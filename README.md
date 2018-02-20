# Sample Web App to Demonstrate eBay Oauth
This sample shows you how to use eBay Oauth and deploy it in [Google App Engine](https://cloud.google.com/appengine/docs/java/).


## Before running the app
1. Register a new Application in https://developer.ebay.com
2. Update [DemoServlet]https://github.com/sengopal/appengine-try-java/blob/master/src/main/java/myapp/DemoServlet.java with the client_id and client_secret

## Before you begin Google Cloud Deployment
1.  Download and install the [Google Cloud
    SDK](https://cloud.google.com/sdk/docs/).
1.  [Install and configure Apache Maven](http://maven.apache.org/index.html).
1.  [Create a new Google Cloud Platform project, or use an existing
		one](https://console.cloud.google.com/project).
1.  Initialize the Cloud SDK.

        gcloud init

1.  Install the Cloud SDK `app-engine-java` component.

        gcloud components install app-engine-java

## Deploying to App Engine

To run the application locally, use the [Maven App Engine
plugin](https://cloud.google.com/appengine/docs/java/tools/using-maven).

    mvn clean appengine:run

View the app at [localhost:8080](http://localhost:8080).

To deploy the app to App Engine, run

    mvn clean appengine:deploy

After the deploy finishes, you can view your application at
`https://YOUR_PROJECT.appspot.com`, where `YOUR_PROJECT` is your Google Cloud
project ID. You can see the new version deployed on the [App Engine section of
the Google Cloud Console](https://console.cloud.google.com/appengine/versions).

