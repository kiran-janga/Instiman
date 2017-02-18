package com.andro.jk.metisandroid1.Management;


import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.andro.jk.metisandroid1.Models.HistoryModel;
import com.andro.jk.metisandroid1.Models.WorkerModel;
import com.andro.jk.metisandroid1.R;
import com.andro.jk.metisandroid1.SaveSharedPreference;
import com.andro.jk.metisandroid1.User.ComplaintActivity;
import com.andro.jk.metisandroid1.WebUtils.RestClient;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class UnattendedDescriptionFragment extends Fragment implements AdapterView.OnItemSelectedListener,AddNumberDialogFragment.AddNumberDialogListener,ChangeCategoryDialogFragment.ChangeCategoryDialogListener {

    HistoryModel unattendedObject;
    TextView mudDate,mudSentBy,mudArea,mudDescription,mudTitle,mudLocation,mudSentByNumber;
    EditText mudComment;
    ImageView mudImage,mudAddWorker,mudChangeCategoryFab,mudSimilarComplaintFab,mudComplete,mudCall;;
    Button mudAssign,mudReject;
    Spinner spinner,mudAssignWorker;
    String assignedPriority,strStatus,strComment;
    int workerId,user_id,received_id,intPriority;
    int receivedCategoryId;
    Button testButton;
    int requestID = (int) System.currentTimeMillis();
    int flags = PendingIntent.FLAG_CANCEL_CURRENT;
    ArrayAdapter<String> workerAdapter;

    ProgressDialog loading;

    final List<String> workers = new ArrayList<String>();
    final List<String> workerNames = new ArrayList<String>();
    final List<Integer> workerIds = new ArrayList<Integer>();
    final List<String> workerNumbers = new ArrayList<String>();
    final List<String> priorityArrayList = new ArrayList<String>();

    String token;
    String access_token;
    String access_token_wo_quotes;
    String s;

    Boolean sp1 = true;
    Boolean sp2 = true;
    Boolean spbPriority = true;
    Boolean spbWorker = true;
    String[] splitTime;

    public static UnattendedDescriptionFragment newInstance(Object object) {

        UnattendedDescriptionFragment fragment = new UnattendedDescriptionFragment();

        Bundle bundle = new Bundle();

        bundle.putSerializable("item", (Serializable) object);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unattendedObject = (HistoryModel) getArguments().getSerializable("item");

        token = "Token ";
        access_token = SaveSharedPreference.getAccessToken(getContext());
        access_token_wo_quotes = access_token.replace("\"", "");
        s = token.concat(access_token_wo_quotes);



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.unattended_description, container, false);


        loading = new ProgressDialog(getContext());
        loading.setMessage("Fetching Data");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setIndeterminate(true);
        loading.show();

        spinner = (Spinner) view.findViewById(R.id.mudAssignPriority);
        spinner.setOnItemSelectedListener(this);

        mudAssignWorker = (Spinner) view.findViewById(R.id.mudAssignWorker);
        mudAssignWorker.setOnItemSelectedListener(this);

        RestClient.get().getWorkers(s, new Callback<List<WorkerModel>>() {
            @Override
            public void failure(RetrofitError error) {

                Log.d("gettingWorkers", "failed");
                loading.dismiss();
                Toast.makeText(getContext(),"Error fetching data, Try after some time",Toast.LENGTH_SHORT).show();
                getActivity().finish();

            }


            @Override
            public void success(List<WorkerModel> workerModels, Response response) {

//                workerIds.add(0);
//                workerNumbers.add("0");
//                workerNames.add("0");
//                //workers.add(workerModel.getUser().first_name+" : "+workerModel.getUser().username);
                workers.add("Assign Worker");

                for (WorkerModel workerModel : workerModels) {
                    //workers.add(workerModel.getWorkerName());
                    workerIds.add(workerModel.getUser().id);
                    workerNumbers.add(workerModel.getUser().username);
                    workerNames.add(workerModel.getUser().first_name);
                    workers.add(workerModel.getUser().first_name + " : " + workerModel.getUser().username);
                    Log.d("msg1", workerNumbers.toString());
                    Log.d("msg2", workerIds.toString());

                }


                workerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, workers);

                workerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                mudAssignWorker.setAdapter(workerAdapter);
                //spinner.setPrompt("Select Category");

                loading.dismiss();


        mudChangeCategoryFab = (ImageView) view.findViewById(R.id.mudChangeCategoryFab);

        mudChangeCategoryFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                receivedCategoryId = Integer.parseInt(unattendedObject.getArea());
                changeCategoryDialog();
            }
        });

        mudSimilarComplaintFab = (ImageView) view.findViewById(R.id.mudSimilarComplaintFab);

        mudSimilarComplaintFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getContext()).setTitle("Confirm")
                        .setMessage("Are you sure?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                loading = new ProgressDialog(getContext());
                                loading.setMessage("Removing Complaint");
                                loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                loading.setIndeterminate(true);
                                loading.show();
                                user_id = unattendedObject.getId();
                                strStatus = "similar";
                                strComment = "Rejected because a similar complaint exits";

                                RestClient.get().rejectSimilar(s, user_id, strComment, strStatus, new Callback<JSONObject>() {
                                    @Override
                                    public void success(JSONObject jsonObject, Response response) {

                                        Intent i = new Intent(getContext(), ManagementMainActivity.class);
                                        i.putExtra("key1", "Submitted");
                                        i.putExtra("key2", "other");
                                        i.putExtra("object", unattendedObject);
                                        loading.dismiss();
                                        Toast.makeText(getContext(), "Complaint removed because of similarity", Toast.LENGTH_SHORT).show();
                                        startActivity(i);

                                    }

                                    @Override
                                    public void failure(RetrofitError error) {

                                        loading.dismiss();
                                        Toast.makeText(getContext(), "Failed to remove complaint, Try after some time", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();


                };

        });

        mudDate= (TextView) view.findViewById(R.id.mudDate);
        mudSentBy = (TextView) view.findViewById(R.id.mudSentBy);
                mudSentByNumber = (TextView) view.findViewById(R.id.mudSentByNumber);
        //mudArea = (TextView) view.findViewById(R.id.mudArea);
        mudImage = (ImageView) view.findViewById(R.id.mudImage);
        mudDescription = (TextView) view.findViewById(R.id.mudDescription);
        mudTitle = (TextView) view.findViewById(R.id.mudTitle);
        mudLocation = (TextView) view.findViewById(R.id.mudLocation);
        mudAssign = (Button) view.findViewById(R.id.mudAssign);
        mudAddWorker = (ImageView) view.findViewById(R.id.mudAddWorker);
        //testButton = (Button) view.findViewById(R.id.testButton);
//
        mudReject = (Button) view.findViewById(R.id.mudReject);

        mudComment = (EditText) view.findViewById(R.id.mudComment);
                mudComplete = (ImageView) view.findViewById(R.id.mudComplete);
                mudCall = (ImageView) view.findViewById(R.id.mudCall);



        splitTime = unattendedObject.getTimestamp().toString().split(" ");

        mudDate.setText(splitTime[1]+" "+ splitTime[2]);
        mudTitle.setText(unattendedObject.getTitle());
                mudSentByNumber.setText(unattendedObject.getNumber());
        //mudId.setText(Integer.toString(unattendedObject.getId()));
        mudSentBy.setText(unattendedObject.getUser());
        //mudArea.setText(unattendedObject.getArea());

        mudDescription.setText("         " + unattendedObject.getDescription());
        mudLocation.setText("          " + unattendedObject.getLocation());

        mudAddWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWorkerDialog();
            }
        });

        Picasso.with(getContext()).load(getResources().getString(R.string.IMAGE_URL) + unattendedObject.getImage()).into(mudImage);




        priorityArrayList.add("Select Priority");
        priorityArrayList.add("Routine");
        priorityArrayList.add("Important");
        priorityArrayList.add("Very Important");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,priorityArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (sp1) {
                    priorityArrayList.remove("Select Priority");
                    adapter.notifyDataSetChanged();
                    sp1 = false;

                }

                spbPriority = false;

                return false;

            }

        });


                mudCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:" + unattendedObject.getNumber()));
                            startActivity(callIntent);
                        } catch (ActivityNotFoundException activityException) {
                            Toast.makeText(getContext(), "Cannot Call", Toast.LENGTH_SHORT).show();
                            Log.e("Calling a Phone Number", "Call failed", activityException);
                        }
                    }


                });
                mudComplete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new AlertDialog.Builder(getContext()).setTitle("Confirm")
                                .setMessage("Are you sure?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        loading = new ProgressDialog(getContext());
                                        loading.setMessage("Updating Complaint");
                                        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                        loading.setIndeterminate(true);
                                        loading.show();


                                        RestClient.get().fixComplaint(s, unattendedObject.getId(), "fixed", new Callback<JSONObject>() {
                                            @Override
                                            public void success(JSONObject jsonObject, Response response) {

                                                Intent i = new Intent(getContext(), ManagementMainActivity.class);
                                                startActivity(i);
                                            }

                                            @Override
                                            public void failure(RetrofitError error) {

                                                loading.dismiss();
                                                Toast.makeText(getContext(), "Updating Failed, Try After Some Time", Toast.LENGTH_SHORT).show();

                                            }

                                        });
                                    }
                                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

                    }

                    ;
                });



        mudAssignWorker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (sp2) {
//                    workerIds.remove(0);
//                    workerNumbers.remove("0");
//                    workerNames.remove("0");
                    workers.remove("Assign Worker");
                    workerAdapter.notifyDataSetChanged();
                    Log.d("avi31", workers.toString());
                    sp2 = false;
                }

                spbWorker = false;

                return false;

            }

        });



        mudAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spbPriority) {
                    Toast.makeText(getContext(), "Please Select Priority", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (spbWorker) {
                    Toast.makeText(getContext(), "Please Assign Worker", Toast.LENGTH_SHORT).show();
                    return;
                }



                loading = new ProgressDialog(getContext());
                loading.setMessage("uploading complaint");
                loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                loading.setIndeterminate(true);
                loading.show();

                String date = new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date());
                strComment = mudComment.getText().toString();

                user_id = unattendedObject.getId();
                strStatus = "In Process";



                RestClient.get().assignComplaint(s, workerId, user_id, strStatus, strComment, intPriority, new Callback<JSONObject>() {
                    @Override
                    public void success(JSONObject jsonObject, Response response) {

                        Toast.makeText(getContext(), "Complaint successfully updated", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getContext(), ManagementMainActivity.class);
                        i.putExtra("key1","Submitted");
                        i.putExtra("key2","In Process");
                        i.putExtra("object",unattendedObject);
                        startActivity(i);
                        loading.dismiss();


                    }

                    @Override
                    public void failure(RetrofitError error) {
                        loading.dismiss();
                        Toast.makeText(getContext(), "Assigning Complaint Failed, Try After Some Time", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        mudReject.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getContext()).setTitle("Confirm")
                        .setMessage("Are you sure?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                loading = new ProgressDialog(getContext());
                                loading.setMessage("Removing Complaint");
                                loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                loading.setIndeterminate(true);
                                loading.show();
                                user_id = unattendedObject.getId();
                                strStatus = "rejected";
                                String date = new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date());
                                strComment = date + "\n" + mudComment.getText().toString();

                                RestClient.get().rejectComplaint(s, user_id, strStatus, strComment, new Callback<JSONObject>() {
                                    @Override
                                    public void success(JSONObject jsonObject, Response response) {
                                        Toast.makeText(getContext(), "Complaint Removed Successfully", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getContext(), ManagementMainActivity.class);
                                        i.putExtra("key1", "Submitted");
                                        i.putExtra("key2", "other");
                                        i.putExtra("object", unattendedObject);
                                        loading.dismiss();
                                        startActivity(i);
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        loading.dismiss();
                                        Toast.makeText(getContext(), "Rejecting Complaint Failed, Try After Some Time", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });

//        testButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getContext(), ManagementMainActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//                pIntent = PendingIntent.getActivity(getContext(), requestID, i, flags);
//
//                int iconRes = R.mipmap.ic_launcher;
//
//                createNotification(0,iconRes,"New Complaint","testing");
//            }
//        });
//
            }

        });

                return view;

    }




    private void addWorkerDialog() {
        FragmentManager fm = getFragmentManager();
        AddNumberDialogFragment addNumberDialogFragment = AddNumberDialogFragment.newInstance("Add Worker");
        // SETS the target fragment for use later when sending results
        addNumberDialogFragment.setTargetFragment(UnattendedDescriptionFragment.this, 300);
        addNumberDialogFragment.show(fm, "fragment_add_worker");
    }

    private void changeCategoryDialog() {

        Log.d("key21","got here");
        FragmentManager fm = getFragmentManager();
        ChangeCategoryDialogFragment changeCategoryDialogFragment = ChangeCategoryDialogFragment.newInstance("Confirm Change Category",receivedCategoryId);
        // SETS the target fragment for use later when sending results
        changeCategoryDialogFragment.setTargetFragment(UnattendedDescriptionFragment.this, 200);
        changeCategoryDialogFragment.show(fm, "fragment_change_category");
    }


    @Override
    public void onFinishAddNumber(final String name, final String number) {


        loading = new ProgressDialog(getContext());
        loading.setMessage("Adding worker");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setIndeterminate(true);
        loading.show();

        RestClient.get().addWorker(s,number,number,name ,new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {


                received_id = Integer.valueOf(jsonObject.get("id").toString());
                workerNumbers.add(number);
                workerIds.add(received_id);
                workerNames.add(name);
                workers.add(name+" : "+number);
                loading.dismiss();
                Toast.makeText(getContext(), "Worker added Successfully", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void failure(RetrofitError error) {
                loading.dismiss();
                Toast.makeText(getContext(),"Adding worker failed, Try Again",Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;

        if(spinner.getId() == R.id.mudAssignWorker) {
            workerId = workerIds.get(position);
            Log.d("workerId", String.valueOf(workerId));
        } else if(spinner.getId() == R.id.mudAssignPriority)
        {
            assignedPriority = parent.getItemAtPosition(position).toString();
            if (assignedPriority.equals("Routine")){
                intPriority=1;
            }else if (assignedPriority.equals("Important")){
                intPriority=2;
            }else if (assignedPriority.equals("Very Important")){
                intPriority=3;
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onFinishChangeCategory(int categoryId) {
        loading = new ProgressDialog(getContext());
        loading.setMessage("Changing Category");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setIndeterminate(true);
        loading.show();

        user_id = unattendedObject.getId();
        RestClient.get().changeCategory(s, user_id, categoryId, new Callback<JSONObject>() {
            @Override
            public void success(JSONObject jsonObject, Response response) {
                Toast.makeText(getContext(), "Sucessfully changed category", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), ManagementMainActivity.class);
                i.putExtra("key1", "Submitted");
                i.putExtra("key2", "other");
                i.putExtra("object",unattendedObject);
                loading.dismiss();
                startActivity(i);
            }

            @Override
            public void failure(RetrofitError error) {
                loading.dismiss();
                Toast.makeText(getContext(), "Failed to change category, Try again", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
