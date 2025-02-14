# CI Debug Notes

To validate some circleci stuff, I was able to run a “build locally” using the steps below.
The local build runs in a docker container.

- (Once) Install circleci client (`brew install circleci`)

- Convert the “real” config.yml into a self contained (non-workspace) config via:

      circleci config process .circleci/config.yml > .circleci/local-config.yml

- Run a local build with the following command:

      circleci local execute -c .circleci/local-config.yml --job 'build'

  Typically, both commands are run together:

      circleci config process .circleci/config.yml > .circleci/local-config.yml && circleci local execute -c .circleci/local-config.yml --job 'build'

  With the above command, operations that cannot occur during a local build will show an error like this:

      ... Error: FAILED with error not supported

  However, the build will proceed and can complete “successfully”, which allows you to verify scripts in your config, etc.

  If the build does complete successfully, you should see a happy yellow `Success!` message.

## Challenges

Apple Mac M1 devices have some problems with running Docker, so you may come across a problem
when running the `circleci local execute` command where it fails with an error including
`OCI runtime create failed`. This issue is caused by Docker/M1 compatability and workarounds
are shown on the [circleci-cli issues page](https://github.com/CircleCI-Public/circleci-cli/issues/672).

## Miscellaneous

To allow your CI build to push changes back to github (e.g. release tags, etc), you need to create setup
a github "Deploy Key" with write access. The command below will create such a key. Use an empty password.
See the [CircliCI SSH document](https://circleci.com/docs/2.0/add-ssh-key/#steps).

    ssh-keygen -m PEM -t rsa -b 4096 -C "community-group@sonatype.com" -f <project-name>_github_rsa.key

Paste the public key into a new "write" enabled GitHub deploy key with Title: CircleCI Write \<project name>

Be sure you check the "Allow write access" option.

    cat <project-name>_github_rsa.key.pub | pbcopy

In the CircleCI Web UI, under Permissions -> SSH Permissions -> Add SSH Key, enter "Hostname": github.com

Paste the private key.

    cat <project-name>_github_rsa.key | pbcopy

As a sanity check, the private key should end with `-----END RSA PRIVATE KEY-----`.

Also update the `ssh-fingerprints:` tag in your config.yml to append the fingerprint of the write key.
