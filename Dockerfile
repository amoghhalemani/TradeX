FROM ubuntu:latest
LABEL authors="reipapa"

ENTRYPOINT ["top", "-b"]