import r2
import json
import csv

jsonfile = 'component_waivers.json'
csvfile = 'component_waivers.csv'


def makeComponentWaiversReport():
    initReport()

    componentWaivers = r2.getNexusIqData('reports/components/waivers')

    with open(jsonfile, 'w') as fd:
            json.dump(componentWaivers, fd, indent=4)


    writeWaiversToCsv('application', componentWaivers['applicationWaivers'])
    writeWaiversToCsv('repository', componentWaivers['repositoryWaivers'])

    print(jsonfile)
    print(csvfile)


def initReport():
    with open(csvfile, 'w') as fd:
            writer = csv.writer(fd)

            line = []
            line.append("ApplicationName")
            line.append("Stage")
            line.append("PackageUrl")
            line.append("PolicyName")
            line.append("ThreatLevel")
            line.append("Comment")
            line.append("CreateDate")
            line.append("ExpiryTime")

            writer.writerow(line)

    return


def writeWaiversToCsv(waiversType, componentWaivers):

    with open(csvfile, 'a') as fd:
            writer = csv.writer(fd)

            for waiver in componentWaivers:

                if waiversType == 'application':
                    applicationName = waiver['application']['publicId']
                else:
                    applicationName = waiver['repository']['publicId']

                stages = waiver['stages']

                for stage in stages:
                    stageId = stage['stageId']
                    componentPolicyViolations = stage['componentPolicyViolations']

                    for componentPolicyViolation in componentPolicyViolations:
                        packageUrl = componentPolicyViolation["component"]["packageUrl"]
                        waivedPolicyViolations = componentPolicyViolation['waivedPolicyViolations']

                        for waivedPolicyViolation in waivedPolicyViolations:
                            policyName = waivedPolicyViolation['policyName']
                            threatLevel = waivedPolicyViolation['threatLevel']
                            comment = waivedPolicyViolation['policyWaiver']['comment']

                            if "createTime" in waivedPolicyViolation['policyWaiver'].keys():
                                createDate = waivedPolicyViolation['policyWaiver']['createTime']

                                if "expiryTime" in waivedPolicyViolation['policyWaiver'].keys():
                                    expiryTime = waivedPolicyViolation['policyWaiver']['expiryTime']
                                else:
                                    expiryTime = "non-expiring"

                            else:
                                createDate = "needs re-eval"

                                if waiversType == 'application':
                                    expiryTime = "needs re-eval"
                                else:
                                    expiryTime = "n/a"

                            if comment is not None:
                                if "\n" in comment:
                                    comment = comment.replace("\n", "-")

                                if "," in comment:
                                    comment = comment.replace(",", "|")
                            else:
                                comment = ""

                            createDate =  r2.formatDate(createDate)
                            expiryTime =  r2.formatDate(expiryTime)

                            line = []
                            line.append(applicationName)
                            line.append(stageId)
                            line.append(packageUrl)
                            line.append(policyName)
                            line.append(str(threatLevel))
                            line.append(comment)
                            line.append(createDate)
                            line.append(expiryTime)

                            writer.writerow(line)

    fd.close()

    return
