import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import { useParams } from "react-router-dom";
import RequestTypeForm from "main/components/RequestTypes/RequestTypeForm";
import { Navigate } from "react-router-dom";
import { useBackend, useBackendMutation } from "main/utils/useBackend";
import { toast } from "react-toastify";

export default function RequestTypeEditPage({ storybook = false }) {
  let { id } = useParams();

  const {
    data: requestType,
    _error,
    _status,
  } = useBackend(
    // Stryker disable next-line all : don't test internal caching of React Query
    [`/api/requesttypes?id=${id}`],
    {
      // Stryker disable next-line all : GET is the default, so mutating this to "" doesn't introduce a bug
      method: "GET",
      url: `/api/requesttypes`,
      params: {
        id,
      },
    },
  );

  const objectToAxiosPutParams = (requestType) => ({
    url: "/api/requesttypes",
    method: "PUT",
    params: {
      id: requestType.id,
    },
    data: {
      requestType: requestType.requestType,
    },
  });

  const onSuccess = (requestType) => {
    toast(
      `RequestType Updated - id: ${requestType.id} requestType: ${requestType.requestType}`,
    );
  };

  const mutation = useBackendMutation(
    objectToAxiosPutParams,
    { onSuccess },
    // Stryker disable next-line all : hard to set up test for caching
    [`/api/requesttypes?id=${id}`],
  );

  const { isSuccess } = mutation;

  const onSubmit = async (data) => {
    mutation.mutate(data);
  };

  if (isSuccess && !storybook) {
    return <Navigate to="/requesttypes" />;
  }

  return (
    <BasicLayout>
      <div className="pt-2">
        <h1>Edit Request Type</h1>
        {requestType && (
          <RequestTypeForm
            submitAction={onSubmit}
            buttonLabel={"Update"}
            initialContents={requestType}
          />
        )}
      </div>
    </BasicLayout>
  );
}
