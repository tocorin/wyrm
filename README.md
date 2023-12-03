wyrm - another implementation of a dns-server

# how to

## run server

```shell
java -jar wyrm.jar -p 2832
```

send udp packets from shell

```shell
nc -u 127.0.0.1 6565
```
## SPECS
[RFC-1035 / DOMAIN NAMES - IMPLEMENTATION AND SPECIFICATION](https://www.rfc-editor.org/rfc/rfc1035)