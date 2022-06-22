package com.example.taskmendofeel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home_Fragment extends Fragment {
    private RecyclerView recycler_view;
    private ProgressBar progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.white));
        }

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        progress=v.findViewById(R.id.progress);

        recycler_view=v.findViewById(R.id.recycler_view);

        return v;
    }


    void callNewProducts() {
        final HashMap<String, ArrayList<String>> hashMap = new HashMap<>();
        final ArrayList<String> question = new ArrayList<>();
        final ArrayList<String> profile_photo = new ArrayList<>();
        final ArrayList<String> post_photo = new ArrayList<>();
        final ArrayList<String> fullname = new ArrayList<>();
        final ArrayList<String> publish = new ArrayList<>();
        final ArrayList<String> type = new ArrayList<>();
        final ArrayList<String> chray = new ArrayList<>();

        final String myurl = "https://s3.us-west-2.amazonaws.com/secure.notion-static.com/e8583282-c7a5-4de2-9b7a-269d705b015a/posts.json?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220622%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220622T025326Z&X-Amz-Expires=86400&X-Amz-Signature=be0336e6f163a84f7384b6c8a783791ce1697c5b4c02e12038bb5c05578038e9&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22posts.json%22&x-id=GetObject";
        progress.setVisibility(View.VISIBLE);

        RequestQueue queuereq = Volley.newRequestQueue(getActivity());
        queuereq.getCache().clear();
        StringRequest stringrequest = new StringRequest(Request.Method.GET, myurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.has("profile_photo"))
                            profile_photo.add(object.optString("profile_photo"));
                        else
                            profile_photo.add("N/A");

                        if (object.has("fullname"))
                            fullname.add(object.optString("fullname"));
                        else
                            fullname.add("Anonymous User");

                        if (object.has("question"))
                            question.add(object.optString("question"));
                        else
                            question.add(object.optString("question_text"));

                        if (object.has("publish"))
                            publish.add(object.optString("publish"));
                        else
                            publish.add(object.optString("publishtime"));

                        if (object.has("post_photo"))
                            post_photo.add(object.optString("post_photo"));
                        else
                            post_photo.add("N/A");

                        ArrayList<String> gedata = new ArrayList<>();
                        JSONArray choicearray = object.optJSONArray("choices");
                        if(choicearray != null && choicearray.length() > 0 )
                        {
                            for(int j=0 ; j<choicearray.length() ; j++)
                            {
                                JSONObject object1 = choicearray.getJSONObject(j);
                                gedata.add(String.valueOf(object1.getInt("percentage")));
                            }
                        }
                        else
                        {
                            gedata.add("n/a");
                        }
                        chray.add(String.valueOf(gedata));

                        type.add(object.optString("type"));

                    }

                    hashMap.put("profile_photo", profile_photo);
                    hashMap.put("type", type);
                    hashMap.put("fullname", fullname);
                    hashMap.put("post_photo", post_photo);
                    hashMap.put("question", question);
                    hashMap.put("publish", publish);
                    hashMap.put("chray", chray);


                    if (recycler_view.getAdapter() == null) {
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recycler_view.setLayoutManager(layoutManager);

                        CallSecondAdapter showInList= new CallSecondAdapter(getActivity(), hashMap);
                        recycler_view.setAdapter(showInList);
                    } else {
                        CallSecondAdapter showInList = ((CallSecondAdapter)recycler_view.getAdapter());
                        showInList.refillItems(getActivity(),hashMap);
                    }

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "No product found", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Server Error Please Try Again Later", Toast.LENGTH_SHORT).show();
            }
        });
        stringrequest.setShouldCache(false);
        queuereq.add(stringrequest);
    }



    class CallSecondAdapter extends RecyclerView.Adapter<CallSecondAdapter.ViewHolder> {
        Context context;
        ArrayList<String> profile_photo,type,fullname,post_photo,question,publish,chray;
        private  final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
        private  final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();

        CallSecondAdapter(Activity activity, HashMap<String, ArrayList<String>> hashMap) {
            context = activity;
            profile_photo= hashMap.get("profile_photo");
            fullname= hashMap.get("fullname");
            post_photo= hashMap.get("post_photo");
            question= hashMap.get("question");
            publish= hashMap.get("publish");
            chray= hashMap.get("chray");
            type= hashMap.get("type");

        }

        void refillItems(Activity activity,HashMap<String, ArrayList<String>> hashMap) {
            context = activity;
            profile_photo= hashMap.get("profile_photo");
            fullname= hashMap.get("fullname");
            post_photo= hashMap.get("post_photo");
            question= hashMap.get("question");
            publish= hashMap.get("publish");
            chray= hashMap.get("chray");
            type= hashMap.get("type");
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.home_page_adapter, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            if (profile_photo.get(position).equalsIgnoreCase("n/a") || profile_photo.get(position).equalsIgnoreCase("null"))
                holder.profile_image.setImageResource(R.drawable.def_profile);
            else
                Glide.with(context).load(profile_photo.get(position)).into(holder.profile_image);

            if (post_photo.get(position).equalsIgnoreCase("null"))
                holder.wallimage.setVisibility(View.GONE);
            else
                Glide.with(context).load(post_photo.get(position)).into(holder.wallimage);


            if (type.get(position).equalsIgnoreCase("Polls"))
            {
                holder.poll_layout.setVisibility(View.VISIBLE);
                holder.nframe.setVisibility(View.GONE);
                holder.pol_layout.setVisibility(View.GONE);

                String val=chray.get(position);
                String val1=val.replace("[","");
                String val2=val1.replace("]","");

                String val3[]=val2.split(",");

                holder.htxt.setText(val3[0]);
                holder.vbtxt.setText(val3[1]);


                holder.hprogress.setProgress(Integer.parseInt(holder.htxt.getText().toString().trim()));
                holder.vbprogress.setProgress(Integer.parseInt(holder.vbtxt.getText().toString().trim()));
            }

            holder.profilename.setText(fullname.get(position));

            holder.date_time.setText(publish.get(position));

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                holder.desc.setText(Html.fromHtml(question.get(position),Html.FROM_HTML_MODE_LEGACY));
            }
            else {
                holder.desc.setText(Html.fromHtml(question.get(position)));
            }


            final GestureDetector gd = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onDoubleTap(MotionEvent e) {

                    holder.heart_unfilled.setVisibility(View.GONE);
                    holder.heart_filled.setVisibility(View.VISIBLE);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    prepareAnimation(scaleAnimation);

                    AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                    prepareAnimation(alphaAnimation);

                    AnimationSet animation = new AnimationSet(true);
                    animation.addAnimation(alphaAnimation);
                    animation.addAnimation(scaleAnimation);
                    animation.setDuration(500);

                    holder.heart_filled.startAnimation(animation);


                    holder.circleBg.setVisibility(View.VISIBLE);
                    holder.heart.setVisibility(View.VISIBLE);
                    holder.circleBg.setScaleY(0.1f);
                    holder.circleBg.setScaleX(0.1f);
                    holder.circleBg.setAlpha(1f);
                    holder.heart.setScaleY(0.1f);
                    holder.heart.setScaleX(0.1f);

                    AnimatorSet animatorSet = new AnimatorSet();

                    ObjectAnimator bgScaleYAnim = ObjectAnimator.ofFloat(holder.circleBg, "scaleY", 0.1f, 1f);
                    bgScaleYAnim.setDuration(500);
                    bgScaleYAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
                    ObjectAnimator bgScaleXAnim = ObjectAnimator.ofFloat(holder.circleBg, "scaleX", 0.1f, 1f);
                    bgScaleXAnim.setDuration(500);
                    bgScaleXAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
                    ObjectAnimator bgAlphaAnim = ObjectAnimator.ofFloat(holder.circleBg, "alpha", 1f, 0f);
                    bgAlphaAnim.setDuration(500);
                    bgAlphaAnim.setStartDelay(300);
                    bgAlphaAnim.setInterpolator(DECCELERATE_INTERPOLATOR);

                    ObjectAnimator imgScaleUpYAnim = ObjectAnimator.ofFloat(holder.heart, "scaleY", 0.1f, 1f);
                    imgScaleUpYAnim.setDuration(300);
                    imgScaleUpYAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
                    ObjectAnimator imgScaleUpXAnim = ObjectAnimator.ofFloat(holder.heart, "scaleX", 0.1f, 1f);
                    imgScaleUpXAnim.setDuration(300);
                    imgScaleUpXAnim.setInterpolator(DECCELERATE_INTERPOLATOR);

                    ObjectAnimator imgScaleDownYAnim = ObjectAnimator.ofFloat(holder.heart, "scaleY", 1f, 0f);
                    imgScaleDownYAnim.setDuration(300);
                    imgScaleDownYAnim.setInterpolator(ACCELERATE_INTERPOLATOR);
                    ObjectAnimator imgScaleDownXAnim = ObjectAnimator.ofFloat(holder.heart, "scaleX", 1f, 0f);
                    imgScaleDownXAnim.setDuration(300);
                    imgScaleDownXAnim.setInterpolator(ACCELERATE_INTERPOLATOR);



                    animatorSet.playTogether(bgScaleYAnim, bgScaleXAnim, bgAlphaAnim, imgScaleUpYAnim, imgScaleUpXAnim);
                    animatorSet.play(imgScaleDownYAnim).with(imgScaleDownXAnim).after(imgScaleUpYAnim);

                    animatorSet.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            holder.circleBg.setVisibility(View.GONE);
                            holder.heart.setVisibility(View.GONE);
                        }
                    });
                    animatorSet.start();

                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {

                    return super.onSingleTapConfirmed(e);
                }

                @Override
                public boolean onDoubleTapEvent(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onDown(MotionEvent e) {

                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                    return super.onScroll(e1, e2, distanceX, distanceY);
                }

                @Override
                public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {

                    return true;
                }
            });

            holder.heart_filled.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.heart_unfilled.setVisibility(View.VISIBLE);
                    holder.heart_filled.setVisibility(View.GONE);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    prepareAnimation(scaleAnimation);

                    AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                    prepareAnimation(alphaAnimation);

                    AnimationSet animation = new AnimationSet(true);
                    animation.addAnimation(alphaAnimation);
                    animation.addAnimation(scaleAnimation);
                    animation.setDuration(500);


                    holder.heart_unfilled.startAnimation(animation);
                }
            });

            holder.heart_unfilled.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    holder.heart_unfilled.setVisibility(View.GONE);
                    holder.heart_filled.setVisibility(View.VISIBLE);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    prepareAnimation(scaleAnimation);

                    AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                    prepareAnimation(alphaAnimation);

                    AnimationSet animation = new AnimationSet(true);
                    animation.addAnimation(alphaAnimation);
                    animation.addAnimation(scaleAnimation);
                    animation.setDuration(500);

                    holder.heart_filled.startAnimation(animation);

                }
            });


            holder.wallimage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gd.onTouchEvent(event);

                }
            });




            holder.profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("resp",chray.get(position));
                }
            });

        }

        @Override
        public int getItemCount() {
            return type.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView wallimage,heart_unfilled,heart_filled,heart;
            CircleImageView profile_image;
            TextView date_time,profilename,desc,liketxt,vbtxt,htxt;
            View circleBg;
            LinearLayout poll_layout,pol_layout;
            FrameLayout nframe;
            ProgressBar vbprogress,hprogress;


            ViewHolder(View itemView) {
                super(itemView);
                wallimage = itemView.findViewById(R.id.wallimage);
                profile_image = itemView.findViewById(R.id.profile_image);
                date_time = itemView.findViewById(R.id.date_time);
                profilename = itemView.findViewById(R.id.profilename);
                desc = itemView.findViewById(R.id.desc);
                liketxt = itemView.findViewById(R.id.liketxt);
                heart_unfilled = itemView.findViewById(R.id.heart_unfilled);
                heart_filled = itemView.findViewById(R.id.heart_filled);

                heart = itemView.findViewById(R.id.heart);
                circleBg = itemView.findViewById(R.id.circleBg);

                vbtxt = itemView.findViewById(R.id.vbtxt);
                htxt = itemView.findViewById(R.id.htxt);

                poll_layout = itemView.findViewById(R.id.poll_layout);
                pol_layout = itemView.findViewById(R.id.pol_layout);
                nframe = itemView.findViewById(R.id.nframe);
                vbprogress = itemView.findViewById(R.id.vbprogress);
                hprogress = itemView.findViewById(R.id.hprogress);
            }
        }
    }

    @Override
    public void onResume() {
        callNewProducts();
        super.onResume();
    }

    private static Animation prepareAnimation(Animation animation){
        animation.setRepeatCount(0);
        animation.setRepeatMode(Animation.REVERSE);

        return animation;
    }



}