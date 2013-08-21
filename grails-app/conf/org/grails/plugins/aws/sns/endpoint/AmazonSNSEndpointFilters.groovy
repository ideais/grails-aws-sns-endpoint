package org.grails.plugins.aws.sns.endpoint

import grails.converters.JSON
import groovyx.net.http.*

class AmazonSNSEndpointFilters {

	def filters = {
		all(uri: '/sns/**') {
			before = {
				// get the message type header
				String messageType = request.getHeader('x-amz-sns-message-type')

				// if message doesn't have the message type header, don't process it.
				if (!messageType) {
					return
				}

				// parse the JSON message in the message body
				def message = JSON.parse(request.reader)

				// The signature is based on SignatureVersion 1. If the sig version is something other than 1,
				// throw an exception.
				if (message["SignatureVersion"] == "1") {
					if (isMessageSignatureValid(message)) {
						log.debug "Signature verification succeeded"
					} else {
						log.error "Signature verification failed"
						throw new SecurityException("Signature verification failed.")
					}
				} else {
					log.error "Signature verification failed"
					throw new SecurityException("Unexpected signature version. Unable to verify signature.")
				}

				// process the message based on type.
				if (messageType == "Notification") {
					// this is just a notification
					request.message = message.message
					request.subject = message.subject
					return true

				} else if (messageType == "SubscriptionConfirmation") {
					// TODO: You should make sure that this subscription is from the topic you expect.
					// Compare topicARN to your list of topics that you want to enable to add this endpoint as a subscription.

					// Confirm the subscription by going to the subscribeURL location
					// and capture the return value (XML message body as a string)
					def subscribeUrl = message["SubscribeURL"]
					def topicArn = message["TopicArn"]

					// call the s
					def http = new HTTPBuilder()
					http.get(subscribeUrl) { resp, xml ->
						def subscriptionArn = xml.ConfirmSubscriptionResponse.ConfirmSubscriptionResult.SubscriptionArn
						if (subscriptionArn == topicArn) {
							log.info "Subscription confirmation to ${topicArn}"
						} else {
							log.error "Error while confirming subscription to ${topicArn}"
						}
					}
					return false

				} else if (messageType == "UnsubscribeConfirmation") {
					def topicArn = message["TopicArn"]
					log.info "Unsubscribing to ${topicArn}"
					return false

				} else {
					log.error "Unknown message type: ${messageType}"
					return false
				}
			}
		}
	}

	private boolean isMessageSignatureValid(message) {
		// TODO: validate message
		return true
	}
}
