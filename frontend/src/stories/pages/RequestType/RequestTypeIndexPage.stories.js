import React from "react";
import { apiCurrentUserFixtures } from "fixtures/currentUserFixtures";
import { systemInfoFixtures } from "fixtures/systemInfoFixtures";
import { requestTypeFixtures } from "fixtures/requestTypeFixtures";
import { http, HttpResponse } from "msw";

import RequestTypeIndexPage from "main/pages/RequestType/RequestTypeIndexPage";

export default {
  title: "pages/RequestType/RequestTypeIndexPage",
  component: RequestTypeIndexPage,
};

const Template = () => <RequestTypeIndexPage storybook={true} />;

export const Empty = Template.bind({});
Empty.parameters = {
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
    http.get("/api/requesttypes/all", () => {
      return HttpResponse.json([], { status: 200 });
    }),
  ],
};

export const ThreeItemsOrdinaryUser = Template.bind({});

ThreeItemsOrdinaryUser.parameters = {
  msw: [
    http.get("/api/currentUser", () => {
      return HttpResponse.json(apiCurrentUserFixtures.userOnly);
    }),
    http.get("/api/systemInfo", () => {
      return HttpResponse.json(systemInfoFixtures.showingNeither);
    }),
    http.get("/api/requesttypes/all", () => {
      return HttpResponse.json(requestTypeFixtures.threeRequestTypes);
    }),
  ],
};

export const ThreeTypesAdminUser = Template.bind({});

ThreeTypesAdminUser.parameters = {
  msw: [
    http.get("/api/currentUser", () => {
      return HttpResponse.json(apiCurrentUserFixtures.adminUser);
    }),
    http.get("/api/systemInfo", () => {
      return HttpResponse.json(systemInfoFixtures.showingNeither);
    }),
    http.get("/api/requesttypes/all", () => {
      return HttpResponse.json(requestTypeFixtures.threeRequestTypes);
    }),
    http.delete("/api/requesttypes", () => {
      return HttpResponse.json(
        { message: "Request Type deleted successfully" },
        { status: 200 },
      );
    }),
  ],
};
