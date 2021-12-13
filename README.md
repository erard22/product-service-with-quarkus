# Demo project using Quarkus

## Prerequisites

* Docker installed: https://www.docker.com/
* running database: ```docker run --name postgres-quarkus -p 5432:5432 -e POSTGRES_PASSWORD=somePassword -d postgres```
* AWS CLI installed: https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html
* AWs CDK installed: https://github.com/aws/aws-cdk
* GraalVM installed: https://www.graalvm.org/

## Useful commands and links:  
* start quarkus in development mode: ``mvn quarkus:dev``
* make a native binary using your local GraalVM: ```mvn package -Pnative``` 
* start the application from the jar file: ```java -jar -Xms64M -Xmx128M target/quarkus-app/quarkus-run.jar```
* start the application from the native binary: ```./target/product-service-with-quarkus-1.0.0-SNAPSHOT-runner```
* what pid running on port 8080 _(on MacOS)_ ```netstat -vanp tcp | grep 8080```
* how is the RSS _(resident set size)_ of a process _(on MacOS)_ ```ps -o pid,rss,command -p <PID>```
* Quarkus development page: http://localhost:8080/q/dev/

## How to turn your REST service into an AWS lambda
1) add the AWS lambda extension: ```mvn quarkus:add-extension -Dextensions="quarkus-amazon-lambda-rest"```
2) create a native linux binary using a docker image: ```mvn clean package -Pnative -Dquarkus.native.container-build=true -Dnative-image.xmx=6g```
3) create a folder for cdk: ```mkdir cdk; cd cdk```
4) initiate the cdk project: ```cdk init app --language typescript```
5) define the cdk stack in the file ```lib/cdk-stack.ts```
```typescript
import { SecretValue } from "aws-cdk-lib";
import * as cdk from 'aws-cdk-lib';
import * as lambda from 'aws-cdk-lib/aws-lambda';
import * as apigw from 'aws-cdk-lib/aws-apigateway';
import * as rds from 'aws-cdk-lib/aws-rds';
import * as ec2 from 'aws-cdk-lib/aws-ec2';
import { Construct } from 'constructs';

export class CdkStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    const dbName = 'QuarkusDB';
    const vpc = new ec2.Vpc(this, 'QuarkusAppVPC');

    // database
    const postgres = new rds.DatabaseInstance(this, 'Postgres', {
      engine: rds.DatabaseInstanceEngine.postgres({version: rds.PostgresEngineVersion.VER_12_8}),
      instanceType: ec2.InstanceType.of(ec2.InstanceClass.BURSTABLE2, ec2.InstanceSize.SMALL),
      vpc,
      vpcSubnets: {
        subnetType: ec2.SubnetType.PUBLIC,
      },
      // never do this again!
      credentials: rds.Credentials.fromPassword('postgres', SecretValue.plainText('somePassword')),
      databaseName: dbName
    })
    postgres.connections.allowFromAnyIpv4(ec2.Port.tcp(5432))

    // The Quarkus lambda
    const quarkusMessageLambda = new lambda.Function(this, "QuarkusMessageLambda", {
      runtime: lambda.Runtime.PROVIDED_AL2,
      functionName: 'quarkus-lambda',
      memorySize: 512,
      handler: "io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest",
      code: lambda.Code.fromAsset('../target/function.zip'),
      environment: {
        QUARKUS_DATASOURCE_JDBC_URL: `jdbc:postgresql://${postgres.dbInstanceEndpointAddress}:${postgres.dbInstanceEndpointPort}/${dbName}`
      }
    });

    // API gateway
    new apigw.LambdaRestApi(this, 'Endpoint', {
      handler: quarkusMessageLambda
    });
  }
}
```
6) deploy the stack: ```cdk deploy```
