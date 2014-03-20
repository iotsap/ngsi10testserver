In order to enable percent encoded URIs as type names, you have to add the
following two lines to your catalina.properties file:

org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH=true
org.apache.catalina.connector.CoyoteAdapter.ALLOW_BACKSLASH=true

See
http://stackoverflow.com/questions/2291428/jax-rs-pathparam-how-to-pass-a-string-with-slashes-hyphens-equals-too
and
http://tomcat.apache.org/security-6.html
for an explanation.
