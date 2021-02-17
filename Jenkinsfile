node{
    cleanWs()
    try{
        checkout scm
    }catch(hudson.AbortException ex) {
        println "scm var not present, skipping source code checkout" 
    }catch(err){
        println "exception ${err}" 
    } 
    
    stash name: 'workspace', allowEmpty: true, useDefaultExcludes: false
}

intoto_record("build"){
  sh "ls -lta"
  build()
}
  
intoto_record("scan"){  
  scan()
}

intoto_record("package_app"){  
  package_app()
}

docker.image("intoto-porter:demo").inside {
        unstash workspace
        sh("porter create")
        deploy()
        stash workspace
}
