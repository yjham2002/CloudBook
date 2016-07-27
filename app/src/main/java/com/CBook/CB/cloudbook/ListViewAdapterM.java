package com.CBook.CB.cloudbook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapterM extends RecyclerView.Adapter<ListViewAdapterM.ViewHolder> {

    public Context mContext = null;
    public List<ListData> mListData = new ArrayList<>();
    public int item_layout;
    public int addition = 0;

    public ListViewAdapterM(Context mContext, int item_layout) {
        super();
        this.mContext = mContext;
        this.item_layout = item_layout;
    }

    public ListViewAdapterM(Context mContext, int item_layout, int addition) {
        this.mContext = mContext;
        this.item_layout = item_layout;
        this.addition = addition;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ListData mData = mListData.get(position);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.mContent.setText(mData.mContent);
        holder.mText.setText(mData.mTitle);

        if(addition == 0){
            holder.cardview.setOnClickListener(new CardView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new sel_list2(new AsyncCallback() {
                        @Override
                        public void callback() {
                            staticInfo.mAdapter.dataChange();
                        }
                    }).execute();
                    final ListData mData = staticInfo.mAdapter.mListData.get(position);
                    if (mData.mTaken == 2) {
                        Toast.makeText(mContext, "이미 완료된 의뢰입니다.", Toast.LENGTH_LONG).show();
                    } else if (mData.mTaken == 1) {
                        Toast.makeText(mContext, "이미 접수된 의뢰입니다.", Toast.LENGTH_LONG).show();
                    } else if (mData.mTitle.equals(staticInfo.netID)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage("작업을 선택하세요.");
                        builder.setCancelable(true);
                        builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                                .setNeutralButton("상세정보", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent detail = new Intent(mContext, DetailActivity.class);
                                        mContext.startActivity(detail);
                                    }
                                })
                                .setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        new executeQ(new AsyncCallback() {
                                            @Override
                                            public void callback() {
                                                Toast.makeText(mContext, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                                new sel_list4(new AsyncCallback() {
                                                    @Override
                                                    public void callback() {
                                                        staticInfo.mAdapterMe.dataChange();
                                                    }
                                                }).execute();
                                                new sel_list2(new AsyncCallback() {
                                                    @Override
                                                    public void callback() {
                                                        staticInfo.mAdapter.dataChange();
                                                    }
                                                }).execute();
                                            }
                                        }).execute("delete from `dcse_article` where `index`=" + mData);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    } else if (mData.mTaken == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage(mData.mTitle + "님의 #" + mData.index + "\n의뢰를 접수하시겠습니까?");
                        builder.setCancelable(true);
                        builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                                .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        new executeQ(new AsyncCallback() {
                                            @Override
                                            public void callback() {
                                                new sel_list2(new AsyncCallback() {
                                                    @Override
                                                    public void callback() {
                                                        staticInfo.mAdapter.dataChange();
                                                    }
                                                }).execute();

                                                new sel_list3(new AsyncCallback() {
                                                    @Override
                                                    public void callback() {
                                                        staticInfo.mAdaptergo.dataChange();
                                                    }
                                                }).execute();
                                                Toast.makeText(mContext, "접수됨", Toast.LENGTH_LONG).show();
                                                new sendPush(new AsyncCallback() {
                                                    public void callback() {
                                                    }
                                                }).execute(mData.mTitle, staticInfo.netID + "님이 #" + Integer.toString(mData.index) + " 의뢰를 접수했습니다.");
                                            }
                                        }).execute("update `dcse_article` set `takenFlag`= 1, `taker`='" + staticInfo.netID + "' where `index`=" + mData.index);
                                    }
                                })
                                .setNeutralButton("대화", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        staticInfo.caller = mData.mTitle;
                                        Intent ii = new Intent(mContext, ChatClientActivity.class);
                                        mContext.startActivity(ii);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        Toast.makeText(mContext, "비정상적인 접근입니다.", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        else{
            holder.cardview.setOnClickListener(new CardView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new sel_list3(new AsyncCallback() {
                        @Override
                        public void callback() {
                            staticInfo.mAdaptergo.dataChange();
                        }
                    }).execute();
                    final ListData mData = staticInfo.mAdaptergo.mListData.get(position);
                    if (mData.mTaken != 2) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage(mData.mTitle + "님의 #" + mData.index + "\n의뢰 정보를 변경하시겠습니까?");
                        builder.setCancelable(true);
                        builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                                .setNegativeButton("완료", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        new executeQ(new AsyncCallback() {
                                            @Override
                                            public void callback() {
                                                new sel_list3(new AsyncCallback() {
                                                    @Override
                                                    public void callback() {
                                                        staticInfo.mAdaptergo.dataChange();
                                                    }
                                                }).execute();
                                                Toast.makeText(mContext, "완료 처리 되었습니다.", Toast.LENGTH_LONG).show();
                                                new sendPush(new AsyncCallback() {
                                                    public void callback() {
                                                    }
                                                }).execute(mData.mTitle, staticInfo.netID + "님이 #" + Integer.toString(mData.index) + " 의뢰를 완료했습니다.");
                                            }
                                        }).execute("update `dcse_article` set `takenFlag`= 2, `taker`='" + staticInfo.netID + "' where `index`=" + mData.index);
                                    }
                                })
                                .setNeutralButton("포기", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        new executeQ(new AsyncCallback() {
                                            @Override
                                            public void callback() {
                                                new sel_list3(new AsyncCallback() {
                                                    @Override
                                                    public void callback() {
                                                        staticInfo.mAdaptergo.dataChange();
                                                    }
                                                }).execute();
                                                new sel_list2(new AsyncCallback() {
                                                    @Override
                                                    public void callback() {
                                                        staticInfo.mAdapter.dataChange();
                                                    }
                                                }).execute();
                                                Toast.makeText(mContext, "포기 처리 되었습니다.", Toast.LENGTH_LONG).show();
                                                new sendPush(new AsyncCallback() {
                                                    public void callback() {
                                                    }
                                                }).execute(mData.mTitle, staticInfo.netID + "님이 #" + Integer.toString(mData.index) + " 의뢰를 포기했습니다.");
                                            }
                                        }).execute("update `dcse_article` set `takenFlag`= 0, `taker`='" + "#ABANDONED" + "' where `index`=" + mData.index);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        Toast.makeText(mContext, "이미 완료된 의뢰입니다.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        try{
            holder.mDate.setText(TIME_MAXIMUM.calculateTime(format.parse(mData.mDate)));
        }catch(java.text.ParseException e){
            holder.mDate.setText(mData.mDate);
        }
        holder.length.setText(staticInfo.distance(mData.lat, mData.lon, staticInfo.latitude, staticInfo.longitude));
        if(mData.mTaken == 0) holder.status.setBackgroundColor(Color.parseColor("#FFFC7373"));
        else if(mData.mTaken == 1) holder.status.setBackgroundColor(Color.parseColor("#FF6898EB"));
        else holder.status.setBackgroundColor(Color.parseColor("#FF69CB69"));
        holder.status.setFocusable(false);
        holder.mText.setFocusable(false);
        holder.mContent.setFocusable(false);
        holder.mDate.setFocusable(false);

        if (mData.mTitle.equals(staticInfo.netID)){
            holder.cardview.setCardBackgroundColor(mContext.getResources().getColor(R.color.light_gray));
        }else {
            holder.cardview.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mText;
        public TextView mDate;
        public TextView mContent;
        public TextView status;
        public TextView length;
        public CardView cardview;

    public ViewHolder(View itemView) {
        super(itemView);
        mText = (TextView)itemView.findViewById(R.id.mText);
        mDate = (TextView)itemView.findViewById(R.id.mDate);
        mContent = (TextView)itemView.findViewById(R.id.mContent);
        status = (TextView)itemView.findViewById(R.id.status);
        length = (TextView)itemView.findViewById(R.id.length);
        cardview = (CardView)itemView.findViewById(R.id.cardview);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(String mTitle, String mDate, String mContent, int mIndex, String mTaker, int mTaken, double lat, double lon){
        ListData addInfo = new ListData();
        addInfo.mContent = mContent;
        addInfo.mTitle = mTitle;
        addInfo.mDate = mDate;
        addInfo.index = mIndex;
        addInfo.mTaker = mTaker;
        addInfo.mTaken = mTaken;
        addInfo.lat = lat;
        addInfo.lon = lon;
        mListData.add(addInfo);
    }

    public void dataChange(){
        this.notifyDataSetChanged();
    }

}