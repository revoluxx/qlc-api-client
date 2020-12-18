# qlc-api-client

QLC+ WebSocket API client for Java

## What is it ?

A Java library/wrapper for communicating with the *Q Light Controller Plus* web API.
It has been created to ease QLC+ control integration by managing the underlying atypic QLC+ synchronous API over WebSocker, and exposing the available API commands.

## Compatibility

Requires **Java 8+** and a Java API for Websockets implementation (JSR 356) like **Tyrus**.
Tested with *QLC+ 4.12.3*, some API commands may be incompatible with earlier versions.

## How to use ?

Be sure to launch QLC+ in webaccess-enabled mode, with the ` -w` argument.

Add the JAR dependency into your Java (or Android ?) project.
(see pom.xml for other dependencies, Maven central publication comming soon)

You can then configure and instanciate a new **QlcApiClient** by using the Builder.
Before you can send API commands, you must `connect()` the api client instance.
Finally you are good to go to query QLC+ API with the built-in commands catalog **QlcApiQuery** static methods.

Example code:
```
QlcApiClient qlcClient = QlcApiClient.builder().build();
qlcClient.connect();
List<GetElementsListRecord> firstQueryResult = qlcClient.executeQuery(QlcApiQuery.getFunctionsList());
qlcClient.executeQueryWithoutResponse(QlcApiQuery.setFunctionStatus("5", FunctionStatus.RUNNING));
```

## Advanced configuration

QlcApiClient's builder defaults to WebSocket endoint server 127.0.0.1 port 9999.
You can configure these parameters and others in the **QlcApiClient.Builder** instance.

## Links

- QLC+ official website: https://www.qlcplus.org
- QLC+ on GitHub: https://github.com/mcallegari/qlcplus
