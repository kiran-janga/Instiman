package com.andro.jk.metisandroid1.Management;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.andro.jk.metisandroid1.WebUtils.RestClient;
import com.andro.jk.metisandroid1.Worker.WorkerComplaintList;
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

public class AttendedDescriptionFragment extends Fragment implements AdapterView.OnItemSelectedListener,AddNumberDialogFragment.AddNumberDialogListener,ChangeCategoryDialogFragment.ChangeCategoryDialogListener {


    ProgressDialog loading;

    HistoryModel attendedObject;
    TextView madDate,madSentBy,madArea,madDescription,madAssignedBy,
            madAssignedTo,madRating,madTitle,madLocation,madStatus,madShowComment,madSentByNumber;
    EditText madComment;
    ImageView madImage,madChangeCategoryFab,madSimilarComplaintFab,madAddWorker,madCall,madComplete;;
    Button madUpdateComplaint;
    Spinner prioritySpinner,statusSpinner,madAssignWorker;
    String strStatus,strComment,assignedPriority;
    int intPriority,assigned,workerId,complaintId,received_id,receivedCategoryId;

    Boolean sp1 = true;
    Boolean sp2 = true;
    Boolean sp3 = true;

    final List<String> workers = new ArrayList<String>();
    final List<String> workerNames = new ArrayList<String>();
    final List<Integer> workerIds = new ArrayList<Integer>();
    final List<String> workerNumbers = new ArrayList<String>();

    final List<String> priorityArrayList = new ArrayList<String>();
    final List<String> statusArrayList = new ArrayList<String>();

    String token;
    String access_token;
    String access_token_wo_quotes;
    String s;

    Boolean spbPriority = true;
    Boolean spbWorker = true;
    Boolean spbStatus = true;

    String[] splitTime;
    ArrayAdapter<String> workerAdapter;

    public static AttendedDescriptionFragment newInstance(Object object) {

        AttendedDescriptionFragment fragment = new AttendedDescriptionFragment();

        Bundle bundle = new Bundle();

        bundle.putSerializable("item", (Serializable) object);
        fragment.setArguments(bundle);
        return fragment;



    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attendedObject = (HistoryModel) getArguments().getSerializable("item");


        token = "Token ";
        access_token = SaveSharedPreference.getAccessToken(getContext());
        access_token_wo_quotes = access_token.replace("\"", "");
        s = token.concat(access_token_wo_quotes);



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.attended_description, container, false);

        loading = new ProgressDialog(getContext());
        loading.setMessage("Fetching Data");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setIndeterminate(true);
        loading.show();



        prioritySpinner = (Spinner) view.findViewById(R.id.madAssignPriority);
        prioritySpinner.setOnItemSelectedListener(this);

        statusSpinner = (Spinner) view.findViewById(R.id.madAssignStatus);
        statusSpinner.setOnItemSelectedListener(this);

        madAssignWorker = (Spinner) view.findViewById(R.id.madAssignWorker);
        madAssignWorker.setOnItemSelectedListener(this);


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
                workers.add("Assign Worker");

                for (WorkerModel workerModel : workerModels) {
                    //workers.add(workerModel.getWorkerName());
                    workerIds.add(workerModel.getUser().id);
                    workerNumbers.add(workerModel.getUser().username);
                    workerNames.add(workerModel.getUser().first_name);
                    workers.add(workerModel.getUser().first_name + " : " + workerModel.getUser().username);

                }


                workerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, workers);

                workerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                madAssignWorker.setAdapter(workerAdapter);
                //spinner.setPrompt("Select Category");

                loading.dismiss();

                madChangeCategoryFab = (ImageView) view.findViewById(R.id.madChangeCategoryFab);

                madChangeCategoryFab.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        receivedCategoryId = Integer.parseInt(attendedObject.getArea());
                        complaintId = attendedObject.getId();
                        changeCategoryDialog();
                    }
                });

                madSimilarComplaintFab = (ImageView) view.findViewById(R.id.madSimilarComplaintFab);

                madSimilarComplaintFab.setOnClickListener(new View.OnClickListener() {
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


                                        complaintId = attendedObject.getId();
                                        strStatus = "similar";
                                        strComment = "Rejected because a similar complaint exits";

                                        RestClient.get().rejectSimilar(s, complaintId, strComment, strStatus, new Callback<JSONObject>() {
                                            @Override
                                            public void success(JSONObject jsonObject, Response response) {
                                                Intent i = new Intent(getContext(), ManagementMainActivity.class);
                                                i.putExtra("key1", "Submitted");
                                                i.putExtra("key2", "other");
                                                i.putExtra("object", attendedObject);
                                                loading.dismiss();
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


                    }
                });

                madDate = (TextView) view.findViewById(R.id.madDate);
                madSentBy = (TextView) view.findViewById(R.id.madSentBy);
                //madArea = (TextView) view.findViewById(R.id.madArea);
                madImage = (ImageView) view.findViewById(R.id.madImage);
                madDescription = (TextView) view.findViewById(R.id.madDescription);
                madAssignedBy = (TextView) view.findViewById(R.id.madAssignedBy);
                madSentByNumber = (TextView) view.findViewById(R.id.madSentByNumber);
                madShowComment = (TextView) view.findViewById(R.id.madShowComment);
                madAssignedTo = (TextView) view.findViewById(R.id.madAssignedTo);
                madRating = (TextView) view.findViewById(R.id.madRating);
                madTitle = (TextView) view.findViewById(R.id.madTitle);
                madLocation = (TextView) view.findViewById(R.id.madLocation);
                //madStatus = (TextView) view.findViewById(R.id.madStatus);
                madUpdateComplaint = (Button) view.findViewById(R.id.madUpdateComplaint);
                madComment = (EditText) view.findViewById(R.id.madComment);
                madAddWorker = (ImageView) view.findViewById(R.id.madAddWorker);

                madCall = (ImageView) view.findViewById(R.id.madCall);
                madComplete = (ImageView) view.findViewById(R.id.madComplete);

                madCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:" + attendedObject.getNumber()));
                            startActivity(callIntent);
                        } catch (ActivityNotFoundException activityException) {
                            Toast.makeText(getContext(), "Cannot Call", Toast.LENGTH_SHORT).show();
                            Log.e("Calling a Phone Number", "Call failed", activityException);
                        }
                    }


                });

                madComplete.setOnClickListener(new View.OnClickListener() {
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



                                        RestClient.get().fixComplaint(s, attendedObject.getId(), "fixed", new Callback<JSONObject>() {
                                            @Override
                                            public void success(JSONObject jsonObject, Response response) {

                                                Intent i = new Intent(getContext(), WorkerComplaintList.class);
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

                    };
                });

                madAddWorker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addWorkerDialog();
                    }
                });


                splitTime = attendedObject.getTimestamp().toString().split(" ");
                madDate.setText(splitTime[1] + " " + splitTime[2]);


                priorityArrayList.add("Select Priority");
                priorityArrayList.add("Routine");
                priorityArrayList.add("Important");
                priorityArrayList.add("Very Important");


                final ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,priorityArrayList);
                priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                prioritySpinner.setAdapter(priorityAdapter);

                prioritySpinner.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (sp1) {
                            priorityArrayList.remove("Select Priority");
                            priorityAdapter.notifyDataSetChanged();
                            sp1 = false;
                        }
                        spbPriority = false;

                        return false;

                    }

                });


                statusArrayList.add("Select Status");
                statusArrayList.add("pending");
                statusArrayList.add("In Process");



                final ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,statusArrayList);
                statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                statusSpinner.setAdapter(statusAdapter);

                statusSpinner.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (sp2) {
                            statusArrayList.remove("Select Status");
                            statusAdapter.notifyDataSetChanged();
                            sp2 = false;
                        }
                        spbStatus = false;

                        return false;

                    }

                });



                madAssignWorker.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (sp3) {
                            workers.remove("Assign Worker");
                            workerAdapter.notifyDataSetChanged();
                            sp3 = false;
                        }


                        spbWorker = false;

                        return false;

                    }

                });





                //madId.setText(Integer.toString(attendedObject.getId()));
                madSentBy.setText(attendedObject.getUser());
                //madArea.setText(attendedObject.getArea());
                madDescription.setText("        "+attendedObject.getDescription());
                madAssignedTo.setText(attendedObject.getAssignedTo());
                madAssignedBy.setText(attendedObject.getAssignedBy());
                madSentByNumber.setText(attendedObject.getNumber());
                madTitle.setText(attendedObject.getTitle());
                madLocation.setText(attendedObject.getLocation());
                madShowComment.setText(attendedObject.getComment());
                if (attendedObject.getPriority() == 1) {
                    madRating.setText("Routine");
                }else if (attendedObject.getPriority() == 0) {
                    madRating.setText("Routine");
                }else if (attendedObject.getPriority() == 2) {
                    madRating.setText("Important");
                }else if (attendedObject.getPriority() == 3) {
                    madRating.setText("Very Important");
                }
                //madStatus.setText(attendedObject.getStatus());

                Picasso.with(getContext()).load(getResources().getString(R.string.IMAGE_URL) + attendedObject.getImage()).into(madImage);



                madUpdateComplaint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (spbStatus) {
                            Toast.makeText(getContext(), "Please Select Status", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (spbPriority) {
                            Toast.makeText(getContext(), "Please Select Priority", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (spbWorker) {
                            Toast.makeText(getContext(), "Please Assign Worker", Toast.LENGTH_SHORT).show();
                            return;
                        }



                        loading = new ProgressDialog(getContext());
                        loading.setMessage("Updating Complaint");
                        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        loading.setIndeterminate(true);
                        loading.show();

                        String date = new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date());
                        strComment = madComment.getText().toString();
                        complaintId = attendedObject.getId();

                        if (strStatus.equals("Select Status")){
                            Toast.makeText(getContext(), "Select status again", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        RestClient.get().assignComplaint(s, workerId, complaintId, strStatus, strComment,intPriority, new Callback<JSONObject>() {
                            @Override
                            public void success(JSONObject jsonObject, Response response) {



                                Intent i = new Intent(getContext(), ManagementMainActivity.class);
                                i.putExtra("key1", "In Process");
                                i.putExtra("key2", "In Process");
                                i.putExtra("object", attendedObject);
                                loading.dismiss();
                                Toast.makeText(getContext(), "Complaint successfully updated", Toast.LENGTH_SHORT).show();
                                startActivity(i);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                loading.dismiss();
                                Toast.makeText(getContext(), "Assigning Complaint Failed, Try After Some Time", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });


            }

        });



        return  view;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;

        if(spinner.getId() == R.id.madAssignWorker) {
            workerId = workerIds.get(position);
        } else if(spinner.getId() == R.id.madAssignPriority) {
            assignedPriority = parent.getItemAtPosition(position).toString();
            if (assignedPriority.equals("Routine")){
                intPriority=1;
            }else if (assignedPriority.equals("Important")){
                intPriority=2;
            }else if (assignedPriority.equals("Very Important")){
                intPriority=3;
            }
        }else if(spinner.getId() == R.id.madAssignStatus) {
            strStatus = parent.getItemAtPosition(position).toString();

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void addWorkerDialog() {
        FragmentManager fm = getFragmentManager();
        AddNumberDialogFragment addNumberDialogFragment = AddNumberDialogFragment.newInstance("Add Worker");
        // SETS the target fragment for use later when sending results
        addNumberDialogFragment.setTargetFragment(AttendedDescriptionFragment.this, 300);
        addNumberDialogFragment.show(fm, "fragment_add_worker");
    }

    private void changeCategoryDialog() {

        Log.d("key21","got here");
        FragmentManager fm = getFragmentManager();
        ChangeCategoryDialogFragment changeCategoryDialogFragment = ChangeCategoryDialogFragment.newInstance("Confirm Change Category",receivedCategoryId);
        // SETS the target fragment for use later when sending results
        changeCategoryDialogFragment.setTargetFragment(AttendedDescriptionFragment.this, 200);
        changeCategoryDialogFragment.show(fm, "fragment_change_category");
    }


    @Override
    public void onFinishAddNumber(final String name, final String number) {

        loading = new ProgressDialog(getContext());
        loading.setMessage("Adding Worker");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setIndeterminate(true);
        loading.show();
        RestClient.get().addWorker(s, number, number, name, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {


                Log.d("xxx",jsonObject.get("id").toString());
                received_id = Integer.parseInt(jsonObject.get("id").toString());
                workerNumbers.add(number);
                workerIds.add(received_id);
                workerNames.add(name);
                workers.add(name + " : " + number);
                loading.dismiss();
                Toast.makeText(getContext(), "Worker added Successfully", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void failure(RetrofitError error) {
                loading.dismiss();
                Toast.makeText(getContext(), "Adding worker failed, Try Again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onFinishChangeCategory(int categoryId) {

        loading = new ProgressDialog(getContext());
        loading.setMessage("Changing Category");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setIndeterminate(true);
        loading.show();

        Log.d("avi41","getting fired");
        Log.d("avi42", Integer.toString(categoryId));
        RestClient.get().changeCategory(s, complaintId, categoryId, new Callback<JSONObject>() {
            @Override
            public void success(JSONObject jsonObject, Response response) {
                Toast.makeText(getContext(), "Sucessfully changed category", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), ManagementMainActivity.class);
                i.putExtra("key1", "Submitted");
                i.putExtra("key2", "other");
                i.putExtra("object", attendedObject);
                loading.dismiss();
                startActivity(i);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("avi51",error.toString());
                loading.dismiss();
                Toast.makeText(getContext(), "Failed to change category,try again", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
