#!/usr/bin/env python
from twisted.web.server import Site
from twisted.web.resource import Resource
from twisted.internet import reactor
import cgi

class FormPage(Resource):
    # define a resource to respond to GET requests with static html form
    def renderGET(self, request):
        return '<html><body><form method="POST"><input name="####" type="text"/></form></body></html>'
    # allow to accept POST requests
    def renderPOST(self, request):
        return '<html><body>You have successfully submitted</body></html>' % (cgi.escape(request.args["the-field"][0]),)

root = Resource()
root.putChild("form", FormPage())
factory = Site(root)
reactor.listenTCP(8880, factory)
reactor.run()
