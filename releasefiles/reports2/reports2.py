
import application_evaluations
import policy_violations
import component_waivers
import firewall_quarantined

    
def main():
    
    application_evaluations.makeApplicationEvaluationsReport()
    
    policy_violations.makePolicyViolationsReport()
    
    component_waivers.makeComponentWaiversReport()
    
    firewall_quarantined.makeFirewallQuarantinedReport()
    

                
if __name__ == '__main__':
    main()
    