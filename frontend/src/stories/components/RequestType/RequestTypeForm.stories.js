import React from "react";
import RequestTypeForm from "main/components/RequestTypes/RequestTypeForm";
import { requestTypeFixtures } from "fixtures/requestTypeFixtures";

export default {
  title: "components/RequestTypes/RequestTypeForm",
  component: RequestTypeForm,
};

const Template = (args) => {
  return <RequestTypeForm {...args} />;
};

export const Create = Template.bind({});

Create.args = {
  buttonLabel: "Create",
  submitAction: (data) => {
    console.log("Submit was clicked with data: ", data);
    window.alert("Submit was clicked with data: " + JSON.stringify(data));
  },
};

export const Update = Template.bind({});

Update.args = {
  initialContents: requestTypeFixtures.oneRequestType[0],
  buttonLabel: "Update",
  submitAction: (data) => {
    console.log("Submit was clicked with data: ", data);
    window.alert("Submit was clicked with data: " + JSON.stringify(data));
  },
};
