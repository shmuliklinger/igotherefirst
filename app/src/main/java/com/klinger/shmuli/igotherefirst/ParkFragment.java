package com.klinger.shmuli.igotherefirst;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.http.HttpMethodName;
import com.amazonaws.mobile.api.idhluy08iph7.ParkingMobileHubClient;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.amazonaws.mobileconnectors.apigateway.ApiRequest;
import com.amazonaws.mobileconnectors.apigateway.ApiResponse;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.StringUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ParkFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ParkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParkFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String LOG_TAG = ParkFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    Button btn;
    private ParkingMobileHubClient apiClient;

    public ParkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ParkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParkFragment newInstance(String param1, String param2) {
        ParkFragment fragment = new ParkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        apiClient = new ApiClientFactory()
                .credentialsProvider(AWSMobileClient.getInstance().getCredentialsProvider())
                .build(ParkingMobileHubClient.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_park, container, false);
        btn = v.findViewById(R.id.get_available_spot_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String method = "GET";

                final String path = "/parking";

                final String body = "";
                final byte[] content = body.getBytes(StringUtils.UTF8);

                final Map parameters = new HashMap<>();
                parameters.put("lang", "en_US");

                final Map headers = new HashMap<>();

                // Use components to create the api request
                ApiRequest localRequest =
                        new ApiRequest(apiClient.getClass().getSimpleName())
                                .withPath(path)
                                .withHttpMethod(HttpMethodName.valueOf(method))
                                .withHeaders(headers)
                                .addHeader("Content-Type", "application/json")
                                .withParameters(parameters);

                // Only set body if it has content.
                if (body.length() > 0) {
                    localRequest = localRequest
                            .addHeader("Content-Length", String.valueOf(content.length))
                            .withBody(content);
                }

                final ApiRequest request = localRequest;

                // Make network call on background thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d(LOG_TAG,
                                    "Invoking API w/ Request : " +
                                            request.getHttpMethod() + ":" +
                                            request.getPath());

                            final ApiResponse response = apiClient.execute(request);

                            final InputStream responseContentStream = response.getContent();

                            if (responseContentStream != null) {
                                final String responseData = IOUtils.toString(responseContentStream);
                                Log.d(LOG_TAG, response.getStatusCode() + " " + response.getStatusText());
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getActivity(), responseData, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                        } catch (final Exception exception) {
                            Log.e(LOG_TAG, exception.getMessage(), exception);
                            exception.printStackTrace();
                        }
                    }
                }).start();


            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
