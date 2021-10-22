#!/bin/sh -x


java $JVM_OPTS -cp /app/resources:/app/classes:/app/libs/* com.github.ludenus.qa.runner.MainApplication $@

if [ "${SUBMIT_ALLURE_REPORT}" = "true" ]; then

    echo ""
    echo "Prepare to submit allure report ..."
    echo ""

    apk add curl jq zip

    echo ""
    echo "Pack allure results ..."
    echo ""
    zip -r /allure-results.zip /allure-results

    echo ""
    echo "Waiting for ${ALLURE_SERVER_URL}/api/result to become available ..."
    echo ""
    while [[ "200" != "$(curl -v -s -o /dev/null -w '%{http_code}' ${ALLURE_SERVER_URL}/api/result)" ]]; do
      sleep 5;
    done

    echo ""
    echo "Submit allure results ..."
    echo ""
    curl --silent \
         -X POST "${ALLURE_SERVER_URL}/api/result" \
         -H  "accept: */*" -H  "Content-Type: multipart/form-data" \
         -F "allureResults=@allure-results.zip;type=application/x-zip-compressed"  \
    | tee /result.json

    uuid=`cat /result.json | jq -r .uuid`

    echo ""
    echo "Generate allure report ..."
    echo ""
    curl --silent \
         -X POST "${ALLURE_SERVER_URL}/api/report" \
         -H  "accept: */*" \
         -H  "Content-Type: application/json" \
         -d "{\"reportSpec\":{\"path\":[\"path\"],\"executorInfo\":{\"name\":\"junit-runner\",\"type\":\"type\",\"url\":\"http://url\",\"buildOrder\":0,\"buildName\":\"buildName\",\"buildUrl\":\"http://build.url\",\"reportName\":\"reportName\",\"reportUrl\":\"http://report.url\"}},\"results\":[\"${uuid}\"],\"deleteResults\":false}"

fi

if [ -n "${DEBUG_SLEEP_BEFORE_EXIT_SECONDS}" ]; then
  echo "debug sleep ${DEBUG_SLEEP_BEFORE_EXIT_SECONDS} seconds before exit..."
  sleep ${DEBUG_SLEEP_BEFORE_EXIT_SECONDS}
fi