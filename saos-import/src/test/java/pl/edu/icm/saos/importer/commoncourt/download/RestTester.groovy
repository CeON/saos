package pl.edu.icm.saos.importer.commoncourt.download;

@GrabResolver(name='codehaus', root='http://repository.codehaus.org')
@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7')
@Grab(group='oauth.signpost', module='signpost-core', version='1.2.1.2')
@Grab(group='oauth.signpost', module='signpost-commonshttp4', version='1.2.1.2')
import static groovyx.net.http.ContentType.*
import groovy.time.TimeCategory
import groovy.time.TimeDuration
import groovyx.net.http.RESTClient

public class RestTester {
    
    
    public static void main(String[] args) {
        def client = new RESTClient( 'http://orzeczenia.ms.gov.pl' )
        
        def timeStart = new Date()
        
        def limit = 100;
        
        for (int i=0; i<10; i++) {
        
            
            def response = client.get([query: [limit: limit, offset: i*limit], headers: [Accept:"text/xml"], path: "/ncourt-api/judgements"])
        
            def xml = response.getData()
        
            def judgmentIds = xml."**".findAll { it.name() == 'id'}
        
            judgmentIds.eachWithIndex { it, idx -> println i*limit+idx + ":" + it 
                println 'getting details...'
                def judgmentDetails = client.get([query: [id:it], headers: [Accept:"text/xml"], path: "/ncourt-api/judgement/details"])
                println judgmentDetails.getData()."**".findAll { it.name() == 'judgement' }
                println 'getting content...'
                def content = client.get([query: [id:it], headers: [Accept:"text/xml"], path: "/ncourt-api/judgement/content"])
                println content.getData()."**".findAll { it.name() == 'xBlock' }
                
            }
        }
        
        def timeStop = new Date()
        TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
        println duration
        
    }
}
