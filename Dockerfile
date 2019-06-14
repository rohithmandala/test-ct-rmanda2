FROM maven:3.3.9

RUN apt-get update
RUN apt-get install -y zip moreutils
RUN apt-get install git
RUN apt-get install -y maven

#RUN pip install -U pip wheel
#RUN pip install -U awscli boto3>=1.9.2 \
#        pytest pytest-mock pytest-xdist pytest-cov \
#        redis pyyaml docker jsonschema

WORKDIR /opt/workdir

RUN mkdir clinical-trials
RUN cd clinical-trials

COPY * /opt/workdir/clinical-trials/

RUN cd /opt/workdir/clinical-trials/
RUN rm /usr/share/maven/boot/plexus-classworlds-2.6.0.jar
RUN ls -lrt /opt/workdir/clinical-trials/
RUN mvn --version
RUN mvn clean install -DskipTests

ENV PATH /opt/workdir/pipeline-scripts:$PATH
ENV PYTHONPATH /opt/workdir/python-testutils:$PYTHONPATH

