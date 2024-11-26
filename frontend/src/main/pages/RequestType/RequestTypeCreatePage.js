import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import RequestTypeForm from "main/components/RequestTypes/RequestTypeForm";
import { Navigate } from "react-router-dom";
import { useBackendMutation } from "main/utils/useBackend";
import { toast } from "react-toastify";

export default function RequestTypeCreatePage({ storybook = false }) {
  const objectToAxiosParams = (requestType) => ({
    url: "/api/requesttypes/post",
    method: "POST",
    params: {
      requestType: requestType.requestType,
    },
  });

  const onSuccess = (requestType) => {
    toast(
      `New requestType Created - id: ${requestType.id} requestType: ${requestType.requestType}`,
    );
  };

  const mutation = useBackendMutation(
    objectToAxiosParams,
    { onSuccess },
    // Stryker disable next-line all : hard to set up test for caching
    ["/api/requesttypes/all"], // mutation makes this key stale so that pages relying on it reload
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
        <h1>Create New Request Type</h1>
        <RequestTypeForm submitAction={onSubmit} />
      </div>
    </BasicLayout>
  );
}
