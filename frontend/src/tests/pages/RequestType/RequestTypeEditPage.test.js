import { fireEvent, render, waitFor, screen } from "@testing-library/react";
import { QueryClient, QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";
import RequestTypeEditPage from "main/pages/RequestType/RequestTypeEditPage";

import { apiCurrentUserFixtures } from "fixtures/currentUserFixtures";
import { systemInfoFixtures } from "fixtures/systemInfoFixtures";
import axios from "axios";
import AxiosMockAdapter from "axios-mock-adapter";
import mockConsole from "jest-mock-console";

const mockToast = jest.fn();
jest.mock("react-toastify", () => {
  const originalModule = jest.requireActual("react-toastify");
  return {
    __esModule: true,
    ...originalModule,
    toast: (x) => mockToast(x),
  };
});

const mockNavigate = jest.fn();
jest.mock("react-router-dom", () => {
  const originalModule = jest.requireActual("react-router-dom");
  return {
    __esModule: true,
    ...originalModule,
    useParams: () => ({
      id: 17,
    }),
    Navigate: (x) => {
      mockNavigate(x);
      return null;
    },
  };
});

describe("RequestTypeEditPage tests", () => {
  describe("when the backend doesn't return data", () => {
    const axiosMock = new AxiosMockAdapter(axios);

    beforeEach(() => {
      axiosMock.reset();
      axiosMock.resetHistory();
      axiosMock
        .onGet("/api/currentUser")
        .reply(200, apiCurrentUserFixtures.userOnly);
      axiosMock
        .onGet("/api/systemInfo")
        .reply(200, systemInfoFixtures.showingNeither);
      axiosMock.onGet("/api/requesttypes", { params: { id: 17 } }).timeout();
    });

    const queryClient = new QueryClient();
    test("renders header but table is not present", async () => {
      const restoreConsole = mockConsole();

      render(
        <QueryClientProvider client={queryClient}>
          <MemoryRouter>
            <RequestTypeEditPage />
          </MemoryRouter>
        </QueryClientProvider>,
      );
      await screen.findByText("Edit Request Type");
      expect(
        screen.queryByTestId("RequestType-requestType"),
      ).not.toBeInTheDocument();
      restoreConsole();
    });
  });

  describe("tests where backend is working normally", () => {
    const axiosMock = new AxiosMockAdapter(axios);

    beforeEach(() => {
      axiosMock.reset();
      axiosMock.resetHistory();
      axiosMock
        .onGet("/api/currentUser")
        .reply(200, apiCurrentUserFixtures.userOnly);
      axiosMock
        .onGet("/api/systemInfo")
        .reply(200, systemInfoFixtures.showingNeither);
      axiosMock.onGet("/api/requesttypes", { params: { id: 17 } }).reply(200, {
        id: 17,
        requestType: "Grad App",
      });
      axiosMock.onPut("/api/requesttypes").reply(200, {
        id: "17",
        requestType: "Grad School Application",
      });
    });

    const queryClient = new QueryClient();

    test("Is populated with the data provided", async () => {
      render(
        <QueryClientProvider client={queryClient}>
          <MemoryRouter>
            <RequestTypeEditPage />
          </MemoryRouter>
        </QueryClientProvider>,
      );

      await screen.findByTestId("RequestTypeForm-id");

      const idField = screen.getByTestId("RequestTypeForm-id");
      const requestTypeField = screen.getByTestId(
        "RequestTypeForm-requestType",
      );
      const submitButton = screen.getByTestId("RequestTypeForm-submit");

      expect(idField).toBeInTheDocument();
      expect(idField).toHaveValue("17");
      expect(requestTypeField).toBeInTheDocument();
      expect(requestTypeField).toHaveValue("Grad App");

      expect(submitButton).toHaveTextContent("Update");

      fireEvent.change(requestTypeField, {
        target: { value: "Grad School Application" },
      });
      fireEvent.click(submitButton);

      await waitFor(() => expect(mockToast).toBeCalled());
      expect(mockToast).toBeCalledWith(
        "RequestType Updated - id: 17 requestType: Grad School Application",
      );

      expect(mockNavigate).toBeCalledWith({ to: "/requesttypes" });

      expect(axiosMock.history.put.length).toBe(1); // times called
      expect(axiosMock.history.put[0].params).toEqual({ id: 17 });
      expect(axiosMock.history.put[0].data).toBe(
        JSON.stringify({
          requestType: "Grad School Application",
        }),
      ); // posted object
    });

    test("Changes when you click Update", async () => {
      render(
        <QueryClientProvider client={queryClient}>
          <MemoryRouter>
            <RequestTypeEditPage />
          </MemoryRouter>
        </QueryClientProvider>,
      );

      await screen.findByTestId("RequestTypeForm-id");

      const idField = screen.getByTestId("RequestTypeForm-id");
      const requestTypeField = screen.getByTestId(
        "RequestTypeForm-requestType",
      );
      const submitButton = screen.getByTestId("RequestTypeForm-submit");

      expect(idField).toHaveValue("17");
      expect(requestTypeField).toHaveValue("Grad App");
      expect(submitButton).toBeInTheDocument();

      fireEvent.change(requestTypeField, {
        target: { value: "Grad School Application" },
      });
      fireEvent.click(submitButton);

      await waitFor(() => expect(mockToast).toBeCalled());
      expect(mockToast).toBeCalledWith(
        "RequestType Updated - id: 17 requestType: Grad School Application",
      );
      expect(mockNavigate).toBeCalledWith({ to: "/requesttypes" });
    });
  });
});
