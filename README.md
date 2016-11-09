# QCon SF 2016 Workshop: Building a Killr Application with Apache Cassandra

Date: Friday, November 11, 1-4PM

This is meant to be an intermediate workshop on application building with Apache Cassandra and DataStax Enterprise.

### Refresh this project the day before the workshop to make sure you have the latest changes!!

#### Prerequisites

Participants should have:
 * Familiarity with Apache Cassandra or DataStax Enterprise
 * A laptop with the latest docker installed
 * IDE or editor of choice
 * Working environment for compiling/running either Java or NodeJS


#### Environment Setup

1. Clone code repo and tools

  `git clone https://github.com/pmcfadin/qconSF-2016-workshop`

2. Download DataStax Enterprise installer Or Docker
  * If you are using a Mac, docker can be a challenge at some times. Consider using the Mac installer on http://academy.datastax.com Sign up for an account and follow the links to the downloads. Make sure and select Mac install and **not** the tarball.
  * If you are running Windows or Linux, docker is the preferred way to go. You can pull the latest image by `docker pull killrvideo/killrvideo-dse:1.0.0`

3. Running docker image

  * Create a local data directory inside `qconSF-2016-workshop` by `mkdir data`
  * Then start docker image with the following command
  `
  docker run -d -p 9042:9042 --name "killrvideo" -v $PWD/data:/var/lib/cassandra killrvideo/killrvideo-dse:1.0.0
  `

4. Install sample data
  * From the `qconSF-2016-workshop` directory, `bin/cdm install --no-data .`

#### Getting Help
  If you have questions before the workshop, you can use a special Slack channel I have setup on DataStax Academy.
  * Signup for a free account on http://academy.datastax.com
  * You then get a automatic invite to the DSA slack organization. Sign into Slack and find the channel **#qcon** You can flag with with a *@patrick* with any questions.
