import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.issue.fields.CustomField
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.issue.ModifiedValue
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder
import com.atlassian.jira.issue.search.SearchProvider
import com.atlassian.jira.jql.parser.JqlQueryParser
import com.atlassian.jira.web.bean.PagerFilter
import com.atlassian.jira.issue.search.SearchProvider


import org.apache.log4j.Logger
import org.apache.log4j.Level

def log = Logger.getLogger("com.acme.CreateSubtask")
log.setLevel(Level.DEBUG)

def changeIssue(Issue issue){
    log.debug(issue)
    if (issue != null) {
        CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager()
        MutableIssue epicIssue

        //Issue issue = (Issue) ComponentAccessor.issueManager.getIssueByCurrentKey("TEST-590")
        CustomField epicLink = customFieldManager.getCustomFieldObjectByName('Epic Link')
        log.debug(issue.getCustomFieldValue(epicLink))
        if (issue.getCustomFieldValue(epicLink) != null) {
            log.debug(issue)
            epicIssue = (MutableIssue) issue.getCustomFieldValue(epicLink) ;
            CustomField epicExpectedImplementationDate = customFieldManager.getCustomFieldObjectByName('Expected implementation date');
            log.debug issue.getCustomFieldValue(epicExpectedImplementationDate)
            log.debug epicIssue.getCustomFieldValue(epicExpectedImplementationDate)

            epicExpectedImplementationDate.updateValue(null, issue , new ModifiedValue(issue.getCustomFieldValue(epicExpectedImplementationDate), epicIssue.getCustomFieldValue(epicExpectedImplementationDate)), new DefaultIssueChangeHolder());
        }
    }
}



def jqlQueryParser = ComponentAccessor.getComponent(JqlQueryParser.class)
def searchProvider = ComponentAccessor.getComponent(SearchProvider.class)
def issueManager = ComponentAccessor.getIssueManager()
def user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()

def query = jqlQueryParser.parseQuery("project = TEST")
def results = searchProvider.search(query, user, PagerFilter.getUnlimitedFilter())


results.getIssues().each {documentIssue ->
    Issue issue = (Issue) issueManager.getIssueObject(documentIssue.id)
    changeIssue(issue)
}






