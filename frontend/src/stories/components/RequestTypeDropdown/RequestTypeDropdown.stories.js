import React, { useState } from "react";

import SingleRequestDropdown from "main/components/RequestTypeDropdown/RequestTypeDropdown";
import { requestTypeFixtures } from "fixtures/requestFixtures";

export default {
  title: "components/RequestTypeDropdown/RequestTypeDropdown",
  component: SingleRequestDropdown,
};

const Template = (args) => {
  const [requests, setRequests] = useState(args.requests[0]);

  return (
    <SingleRequestDropdown
      requests={requests}
      setRequests={setRequests}
      controlId={"SampleControlId"}
      label={"Request"}
      {...args}
    />
  );
};

export const OneRequest = Template.bind({});
OneRequest.args = {
  requests: requestTypeFixtures.oneRequestType,
};

export const ThreeRequests = Template.bind({});
ThreeRequests.args = {
  requests: requestTypeFixtures.threeRequestTypes,
};
