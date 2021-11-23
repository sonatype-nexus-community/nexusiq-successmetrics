import r2
import json
import csv


jsonfile = 'application_evaluations.json'
csvfile = 'application_evaluations.csv'
    
    
def makeApplicationEvaluationsReport():
    applicationEvaluations = r2.getNexusIqData('reports/applications')

    with open(jsonfile, 'w') as fd:
            json.dump(applicationEvaluations, fd)
    
    with open(csvfile, 'w') as fd:
            writer = csv.writer(fd)
            
            line = []
            line.append("ApplicationName")
            line.append("EvaluationDate")
            line.append("Stage")

            writer.writerow(line)

            for applicationEvaluation in applicationEvaluations:
                stage = applicationEvaluation["stage"]
                evaluationDate = applicationEvaluation["evaluationDate"]
                applicationId = applicationEvaluation["applicationId"]
                applicationName = r2.getApplicationName(applicationEvaluation["reportDataUrl"])
                
                line = []
                line.append(applicationName)
                line.append(evaluationDate)
                line.append(stage)
                        
                writer.writerow(line)
    
    print(jsonfile)
    print(csvfile)

    return

