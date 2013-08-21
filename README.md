grails-aws-sns-endpoint
=======================

This plugin provides a way to receives messages easily from [Amazon SNS](http://aws.amazon.com/sns/) topics.

SNS provides a HTTP subscription, which will send messages as JSON payloads to HTTP endpoints.
Although this seems simple, Amazon defines a mini-protocol which these endpoints must [conform to](http://docs.aws.amazon.com/sns/latest/dg/SendMessageToHttp.html).

This plugin defines a grails filter which intercepts any controller mapped to URI "/sns/**".
The filter implements the subscription protocol as defined by Amazon, removing this burden from your controller.

Your controller get incoming messages from `request.message`, which is just a string (the message body).

