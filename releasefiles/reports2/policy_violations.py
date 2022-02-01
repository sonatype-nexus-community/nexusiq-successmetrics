import r2
import json
import csv


jsonfile = 'policy_violations.json'
csvfile = 'policy_violations.csv'


def makePolicyViolationsReport():
    policies = r2.getNexusIqData('policies')
    policyIds = getPolicyIds(policies)

    policyViolations = r2.getNexusIqData("policyViolations?" + policyIds)

    with open(jsonfile, 'w') as fd:
            json.dump(policyViolations, fd)

    writeToCsvFile(policyViolations)

    print(jsonfile)
    print(csvfile)

    return


def getPolicyIds(data):
    policyIds = ""
    policies = data['policies']

    for policy in policies:
        name = policy["name"]
        id = policy["id"]

        if name in r2.securityPolicyList or name in r2.licensePolicyList:
            policyIds += "p=" + id + "&"

    result = policyIds.rstrip('&')

    return result


def writeToCsvFile(policyViolations):
    applicationViolations = policyViolations['applicationViolations']

    with open(csvfile, 'w') as fd:
            writer = csv.writer(fd)

            line = []
            line.append("PolicyName")
            line.append("Reason")
            line.append("ApplicationName")
            line.append("OpenTime")
            line.append("Component")
            line.append("Stage")

            writer.writerow(line)

            for applicationViolation in applicationViolations:
                applicationPublicId = applicationViolation["application"]["publicId"]

                policyViolations = applicationViolation["policyViolations"]
                for policyViolation in policyViolations:
                    stage = policyViolation["stageId"]
                    openTime = policyViolation["openTime"]
                    policyName = policyViolation["policyName"]
                    packageUrl = policyViolation["component"]["packageUrl"]

                    constraintViolations = policyViolation["constraintViolations"]

                    for constraintViolation in constraintViolations:
                        reason = ""

                        reasons = constraintViolation["reasons"]

                        if policyName == "Integrity-Rating":
                            reason = "Integrity-Rating"
                        elif policyName in r2.securityPolicyList:
                            reason = r2.getCVE(reasons)
                        elif policyName in r2.licensePolicyList:
                            reason = r2.getLicense(reasons)
                        else:
                            reason = ""

                        line = []
                        line.append(policyName)
                        line.append(reason)
                        line.append(applicationPublicId)
                        line.append(openTime)
                        line.append(packageUrl)
                        line.append(stage)

                        writer.writerow(line)

    fd.close()

    return
