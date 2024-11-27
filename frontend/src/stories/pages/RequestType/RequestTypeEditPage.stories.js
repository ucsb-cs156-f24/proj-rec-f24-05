import React from "react";
import { apiCurrentUserFixtures } from "fixtures/currentUserFixtures";
import { systemInfoFixtures } from "fixtures/systemInfoFixtures";
import { http, HttpResponse } from "msw";

import RequestTypeEditPage from "main/pages/RequestType/RequestTypeEditPage";
import { requestTypeFixtures } from "fixtures/requestTypeFixtures";

export default {
  title: "pages/RequestType/RequestTypeEditPage",
  component: RequestTypeEditPage,
};

const Template = () => <RequestTypeEditPage storybook={true} />;

export const Default = Template.bind({});
Default.parameters = {
  msw: [
    http.get("/api/currentUser", () => {
      return HttpResponse.json(apiCurrentUserFixtures.userOnly, {
        status: 200,
      });
    }),
    http.get("/api/systemInfo", () => {
      return HttpResponse.json(systemInfoFixtures.showingNeither, {
        status: 200,
      });
    }),
    http.get("/api/requesttypes", () => {
      return HttpResponse.json(requestTypeFixtures.threeRequestTypes[0], {
        status: 200,
      });
    }),
    http.put("/api/requesttypes", () => {
      return HttpResponse.json({}, { status: 200 });
    }),
    http.put("/api/requesttypes", (req) => {
      window.alert("PUT: " + req.url + " and body: " + req.body);
      return HttpResponse.json({}, { status: 200 });
    }),
  ],
};
