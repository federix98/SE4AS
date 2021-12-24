# Application runtime

FROM openjdk:latest

ADD resources/data /data

RUN cd /data && \
    ./setup_02_osgi-starterkit.sh

CMD ["data/osgi/rt"]