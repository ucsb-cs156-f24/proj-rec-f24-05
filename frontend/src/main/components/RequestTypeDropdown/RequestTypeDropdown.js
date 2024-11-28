import { compareValues } from "main/utils/sortHelper";
import React, { useState } from "react";
import { Form } from "react-bootstrap";

const SingleRequestDropdown = ({
  requests,
  request,
  setrequest = null,
  controlId = null,
  onChange = null,
  label = "Request Area",
}) => {
  const localSearchrequest = localStorage.getItem(controlId);

  const [requestState, setrequestState] = useState(
    // Stryker disable next-line all : not sure how to test/mock local storage
    localSearchrequest || request,
  );

  // Stryker disable all
  const handleRequestOnChange = (event) => {
    localStorage.setItem(controlId, event.target.value);
    setrequestState(event.target.value);
    setrequest(event.target.value);
    if (onChange != null) {
      onChange(event);
    }
  };
  // Stryker restore all

  requests.sort(compareValues("requestCode"));

  return (
    <Form.Group controlId={controlId}>
      <Form.Label>{label}</Form.Label>
      <Form.Control
        as="select"
        value={requestState}
        onChange={handleRequestOnChange}
      >
        {requests.map(function (object) {
          const key = `${controlId}-option-${object.id}`;
          return (
            <option key={key} data-testid={key} value={object.requestType}>
              {object.requestType}
            </option>
          );
        })}
      </Form.Control>
    </Form.Group>
  );
};

export default SingleRequestDropdown;
