import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import SingleRequestDropdown from "../../../main/components/RequestTypeDropdown/RequestTypeDropdown";

describe("SingleRequestDropdown Component", () => {
  const options = [
    { id: 1, requestType: "Scholarship or Fellowship", label: "POST Request" },
    {
      id: 2,
      requestType: "MS program (other than CS Dept BS/MS)",
      label: "PUT Request",
    },
    { id: 3, requestType: "PhD program", label: "DELETE Request" },
  ];

  test("renders without crashing", () => {
    render(
      <SingleRequestDropdown
        requests={options}
        request={options[0]}
        onChange={() => {}}
        controlId="testDropdown"
      />,
    );
    const dropdown = screen.getByRole("combobox");
    expect(dropdown).toBeInTheDocument();
  });

  test("renders all options correctly", () => {
    render(
      <SingleRequestDropdown
        requests={options}
        request={options[0]}
        onChange={() => {}}
        controlId="testDropdown"
      />,
    );
    fireEvent.mouseDown(screen.getByRole("combobox"));
    options.forEach((option) => {
      expect(screen.getByText(option.requestType)).toBeInTheDocument();
    });
  });

  test("renders requests in sorted order by requestCode", () => {
    const unorderedRequests = [
      { id: 3, requestType: "PhD program", requestCode: "B" },
      { id: 1, requestType: "Scholarship or Fellowship", requestCode: "C" },
      {
        id: 2,
        requestType: "MS program (other than CS Dept BS/MS)",
        requestCode: "A",
      },
    ];

    render(
      <SingleRequestDropdown
        requests={unorderedRequests}
        request={unorderedRequests[0]}
        onChange={() => {}}
        controlId="testDropdown"
      />,
    );

    const dropdown = screen.getByRole("combobox");
    fireEvent.mouseDown(dropdown);

    const options = screen.getAllByRole("option");
    const optionTexts = options.map((option) => option.textContent);

    // Assert that options are sorted by requestCode: ['A', 'B', 'C']
    expect(optionTexts).toEqual([
      "MS program (other than CS Dept BS/MS)",
      "PhD program",
      "Scholarship or Fellowship",
    ]);
  });

  test('renders "Request Area" label', () => {
    render(
      <SingleRequestDropdown
        requests={options}
        request={options[0]}
        onChange={() => {}}
        controlId="testDropdown"
      />,
    );
    const labelElement = screen.getByText(/Request Area/i);
    expect(labelElement).toBeInTheDocument();
  });

  test("renders options with correct data-testid attribute", () => {
    render(
      <SingleRequestDropdown
        requests={options}
        request={options[0]}
        onChange={() => {}}
        controlId="testDropdown"
      />,
    );
    fireEvent.mouseDown(screen.getByRole("combobox"));
    options.forEach((option) => {
      const key = `testDropdown-option-${option.id}`;
      expect(screen.getByTestId(key)).toBeInTheDocument();
    });
  });
});
