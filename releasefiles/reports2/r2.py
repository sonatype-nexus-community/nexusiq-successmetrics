import sys
import requests
import json

iqurl = sys.argv[1]
iquser = sys.argv[2]
iqpwd = sys.argv[3]

securityPolicyList = []
securityPolicyList.append("Security-Critical")
securityPolicyList.append("Security-High")
securityPolicyList.append("Security-Medium")
securityPolicyList.append("Security-Malicious")
securityPolicyList.append("Security-Namespace Conflict")
securityPolicyList.append("Integrity-Rating")

licensePolicyList = []
licensePolicyList.append("License-Banned")
licensePolicyList.append("License-None")
licensePolicyList.append("License-Copyleft")
    
iqapi = 'api/v2'


def getNexusIqData(end_point):
    url = "{}/{}/{}" . format(iqurl, iqapi, end_point)

    req = requests.get(url, auth=(iquser, iqpwd), verify=False)

    if req.status_code == 200:
        res = req.json()
    else:
        res = "Error fetching data"

    return res


def getApplicationName(urlPath):
    l = urlPath.split('/')
    return(l[3])


def formatDate(dt):
  d = dt[0:10]
  # d = d.replace("T", ".")
  return d
  

def getLicense(reasons):
    licenses = []
    licenseList  = ""

    for reason in reasons:
        licenseString = reason["reason"]

        fstart = licenseString.find('(')
        fend = licenseString.find(')')

        license = licenseString[fstart:fend]
        license = license[2:-1]

        if not license in licenses:
            licenses.append(license)

    for l in licenses:
        licenseList += l+":"

    licenseList = licenseList[:-1]

    return(licenseList)


def getCVE(reasons):
    cves = []
    cveList = ""

    for reason in reasons:
        reference = reason["reference"]

        if type(reference) is dict:
            cve = reference["value"]

            if not cve in cves:
                cves.append(cve)

    for c in cves:
        cveList += c+":"

    cveList = cveList[:-1]

    return(cveList)


def getCVE2(reasons):
    values = []
    f = ""
    
    for reason in reasons:
        reference = reason["reference"]
        
        if not reference is None:
            newValue = reference["value"]
            if not itemExists(newValue, values):
                values.append(newValue)
        
    for v in values:
        f = f.join(v + ":")
        
    f = f[:-1]
    
    return f


def itemExists(item,items):
    exists = False
    
    for i in items:
        if i == item:
            exists = True
            break
        
    return exists




