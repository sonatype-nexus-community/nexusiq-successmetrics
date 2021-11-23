import r2
import json
import csv
import os 
import shutil


quarantined_datadir = "./quarantined_data"
debug = False


def makeFirewallQuarantinedReport():
    init_report()
    
    summary_report("autoreleased_from_quarantine_summary", "firewall/releaseQuarantine/summary")
    summary_report("quarantined_components_summary", "firewall/quarantine/summary")

    autoreleased_from_quarantine_config()
    
    list_report("autoreleased_from_quarantine_components", "firewall/components/autoReleasedFromQuarantine")
    list_report("quarantined_components", "firewall/components/quarantined")
    


def init_report():

    if os.path.exists(quarantined_datadir):
        shutil.rmtree(quarantined_datadir)
    
    os.mkdir(quarantined_datadir)
    
    return 


def print_jsonfile(json_data, jsonfile):
    output_file = "{}/{}{}".format(quarantined_datadir, jsonfile, ".json")
    json_formatted = json.dumps(json_data, indent=2)
    #print(json_formatted)
    
    with open(output_file, 'w') as outfile:
        json.dump(json_data, outfile, indent=2)
    
    print(output_file)
    return


def summary_report(report_name, end_point):
    data = r2.getNexusIqData(end_point)
    
    if "Error" in data:
        return 
    
    print_jsonfile(data, report_name)

    csvfile = "{}/{}{}".format(quarantined_datadir, report_name, ".csv")
    with open(csvfile, 'w') as fd:
        writer = csv.writer(fd, delimiter=",")
    
        # print header
        line = []
        for key in data.keys():
            line.append(key)
    
        writer.writerow(line)
    
        # print data
        line = []
        for value in data.values():
            line.append(value)
    
        writer.writerow(line)

    print(csvfile)
    return


def autoreleased_from_quarantine_config():
    end_point = "firewall/releaseQuarantine/configuration"
    data = r2.getNexusIqData(end_point)
    print_jsonfile(data, "autoreleased_from_quarantine_config")
    
    csvfile = "{}/{}{}".format(quarantined_datadir, "autoreleased_from_quarantine_config", ".csv")
    with open(csvfile, 'w') as fd:
        writer = csv.writer(fd, delimiter=",")
        
        # print header
        line = []
        line.append("id")
        line.append("name")
        line.append("autoReleaseQuarantineEnabled")

        writer.writerow(line)

        # print data
        for d in data:
            line = []
            line.append(d["id"])
            line.append(d["name"])
            line.append(d["autoReleaseQuarantineEnabled"])
            writer.writerow(line)
            
    print(csvfile)
    
    return


def list_report(report_name, end_point):
    page = 1
    page_size = 250
    page_count, results = page_query(end_point, page, page_size, report_name)
    csvfile = "{}/{}{}".format(quarantined_datadir, report_name, ".csv")
 
    if page_count > 0:
        print(csvfile)

        with open(csvfile, 'w') as fd:
            writer = csv.writer(fd, delimiter=",")
            
            line = []
            line.append("repository")
            line.append("quarantine_date")
            line.append("date_cleared")
            line.append("path_name")
            line.append("format")
            line.append("quarantined")
            line.append("policy_name")
            line.append("threat_level")
            line.append("cve")
            writer.writerow(line)
        
        while page <= page_count:
            
            if len(results) > 0:
                print_list_report(results, csvfile)
                
                if debug:
                    print_jsonfile(results, report_name + "_" + str(page))
            
            page += 1
            page_count,results = page_query(end_point, page, page_size, report_name)
            
    else:
        print(csvfile + " [no data]")
        
    return


def page_query(end_point, page, page_size, report_name):
    asc = True

    if report_name == "autoreleased_from_quarantine_components":
        sort_by = "releaseQuarantineTime"
    else:
        sort_by = "quarantineTime"
        
    query = "{}?page={}&pageSize={}&sortBy={}&asc={}".format(end_point, page, page_size, sort_by, asc)
    data = r2.getNexusIqData(query)
    
    page_count = data["pageCount"]
    results = data["results"]
    
    return (page_count,results)


def print_list_report(results, csvfile):
    
    with open(csvfile, 'a') as fd:
        writer = csv.writer(fd, delimiter=",")
                
        for result in results:
            repository = result["repository"]
            quarantine_date = result["quarantineDate"]
            date_cleared = result["dateCleared"]
            path_name = result["pathname"]
            quarantined = result["quarantined"]
            format = result["componentIdentifier"]["format"]
            
            if result["quarantinePolicyViolations"]:
                for quarantinePolicyViolation in result["quarantinePolicyViolations"]:
                    policy_name = quarantinePolicyViolation["policyName"]
                    threat_level = quarantinePolicyViolation["threatLevel"]
                    
                    for constraint in quarantinePolicyViolation["constraintViolations"]:
                        cve = r2.getCVE2(constraint["reasons"])
                    
                        line = []
                        line.append(repository)
                        line.append(quarantine_date)
                        line.append(date_cleared)
                        line.append(path_name)
                        line.append(format)
                        line.append(quarantined)
                        line.append(policy_name)
                        line.append(threat_level)
                        line.append(cve)
                        writer.writerow(line)
            else:
                line = []
                line.append(repository)
                line.append(quarantine_date)
                line.append(date_cleared)
                line.append(path_name)
                line.append(format)
                line.append(quarantined)
                line.append()
                line.append()
                line.append()
                writer.writerow(line)

    return



