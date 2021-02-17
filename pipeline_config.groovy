libraries{
    in_toto_ex{
        in_toto{
            build{ 
                record{
                    materials = []
                    products = "demo-project/*"
                }
                
                layout{
                    expected_materials = []
                    expected_products = [["CREATE", "demo-project/*"], ["DISALLOW", "*"]]
                }
            }
            scan{
                record{
                    materials = "demo-project/*"
                    products = "scan.log"
                }
                layout{
                    expected_materials = [["MATCH", "demo-project/*", "WITH", "PRODUCTS", "FROM", "build"]]
                    expected_products = [["CREATE", "scan.log"], ["DISALLOW", "*"]]
                }
            }
            
            package_app{
                record{
                    materials =  "demo-project/* scan.log"
                    products = "demo-project.tar.gz"
                }
                layout{
                    expected_materials =  [
                    ["MATCH", "demo-project/*", "WITH", "PRODUCTS", "FROM", "build"], 
                    ["MATCH", "scan.log", "WITH", "PRODUCTS", "FROM", "scan"], ["DISALLOW", "*"]]

                    expected_products = [["CREATE", "demo-project.tar.gz"], ["DISALLOW", "*"]]
                }
            }
            deploy{}
        }
    }

    porter{
        inside_image = "intoto-porter:demo"
    }

    in_toto_utils{
        inside_image = "in-toto-python:demo"
        functionary{
            path = "func" // the private key of the functionary; routine will also generate public key
            generate = true
        }

        layout{
            signer_path = "func" // private key path
            output_file = "the.layout"
            input_json = "layout.json"

            inspect = [[ 
                "name": "untar",
                "expected_materials": [
                    ["MATCH", "demo-project.tar.gz", "WITH", "PRODUCTS", "FROM", "package_app"],
                    ["ALLOW", ".keep"],
                    ["ALLOW", "func.pub"],
                    ["ALLOW", "func"],
                    ["ALLOW", "the.layout"],
                    ["DISALLOW", "*"]
                ],
                "expected_products": [
                    ["MATCH", "demo-project/*", "WITH", "PRODUCTS", "FROM", "build"],
                    ["MATCH", "scan.log", "WITH", "PRODUCTS", "FROM", "scan"],
                    ["ALLOW", "demo-project/.git/*"],
                    ["ALLOW", "demo-project.tar.gz"],
                    ["ALLOW", ".keep"],
                    ["ALLOW", "func.pub"],
                    ["ALLOW", "func"],
                    ["ALLOW", "the.layout"],
                    ["DISALLOW", "*"]
                ],
                "run": [
                    "tar",
                    "xzf",
                    "demo-project.tar.gz",
                ]
            ]]
                
        }
    }

}