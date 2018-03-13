package com.domain.train.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.domain.train.R;
import com.domain.train.models.City;
import com.domain.train.models.Station;

import java.util.LinkedList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.Nullable;

/**
 * Created by Ilya on 05.03.2017.
 */

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolderMy> {
    public int width = 0;
    public int adsPosition = 0;


    public RecyclerView child;

    private OnClickListenerDetail mOnClickListenerDetail;
    private LinkedList<Station> stations = new LinkedList<>();

    public StationAdapter(OnClickListenerDetail onClickListenerDetail) {
        mOnClickListenerDetail = onClickListenerDetail;
    }

    public void setData(LinkedList<Station> stations) {
        if (stations == null)
            return;
        this.stations = stations;
    }

    public void reload() {
        notifyDataSetChanged();
    }

    public void inserted() {
        notifyItemInserted(getItemCount() - 1);
    }

    public void reloadItem(int position) {
        notifyItemChanged(position);
    }


    public Station getItem(int i) {
        if (stations.size() > i && i >= 0) {
            return stations.get(i);
        } else {
            return null;
        }
    }


    @Override
    public long getItemId(int i) {
        return getItem(i).getId();
    }

    @Override
    public int getItemCount() {
        if (stations == null)
            return 0;
        return stations.size();
    }

    @Override
    public ViewHolderMy onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_station, parent, false);
        }
        return new ViewHolderMy(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolderMy holder, final int position) {

        int type = getItemViewType(position);

        ViewHolderMy holders = (ViewHolderMy) holder;


        holders.bind(getItem(position), type);

        holder.itemSelectAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListenerDetail.onClickSelect(view, position);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        int typePost = 1;

        return typePost;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public interface OnClickListenerDetail {
        void onClickSelect(View v, int position);
    }

    public class ViewHolderMy extends RecyclerView.ViewHolder {

        public View view;

        @Nullable
        @BindView(R.id.item_station_container)
        RelativeLayout itemContainer;
        @BindView(R.id.item_station_title)
        TextView itemTitle;
        @BindView(R.id.item_station_city_title)
        TextView itemCityTitle;
        @BindView(R.id.item_station_address)
        TextView itemAddress;
        @BindView(R.id.item_station_select)
        Button itemSelectAction;

        ViewHolderMy(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }

        public void bind(Station station, int type) {
            if (station == null)
                return;

            itemTitle.setText(station.getStationTitle());
            itemCityTitle.setText(station.getCountryTitle() + ", " + station.getCityTitle());
            itemAddress.setText(station.getFullAddress());

            int position = getAdapterPosition();

            if (position > 0) {
                if (getItem(position - 1).getCityTitle().equals(station.getCityTitle())) {
                    itemCityTitle.setVisibility(View.GONE);
                } else {
                    itemCityTitle.setVisibility(View.VISIBLE);
                }
            } else
                itemCityTitle.setVisibility(View.VISIBLE);

        }
    }
}


