node{
    cleanWs()
    try{
        checkout scm
    }catch(AbortException ex) {
        println "scm var not present, skipping source code checkout" 
    }catch(err){
        println "exception ${err}" 
    } 
    
    stash name: 'workspace', allowEmpty: true, useDefaultExcludes: false
}

wrap_around{
  sh "ls -lt"
  build()
}
  
scan()
deploy()