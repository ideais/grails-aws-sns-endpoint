grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolution = {
	inherits("global") {
	}
	log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
	legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility
	repositories {
		grailsCentral()
		mavenCentral()
	}
	dependencies {
		compile 'org.codehaus.groovy.modules.http-builder:http-builder:0.5.2'
	}

	plugins {
		build(":tomcat:$grailsVersion",
				":release:2.2.1",
				":rest-client-builder:1.0.3") {
			export = false
		}
	}
}
