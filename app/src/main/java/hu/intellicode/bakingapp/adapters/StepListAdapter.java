// For RecyclerView OnClick implementation I used the description of Antonio Leive: https://antonioleiva.com/recyclerview-listener/

package hu.intellicode.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.intellicode.bakingapp.R;
import hu.intellicode.bakingapp.models.Step;

//Adapter for step list of the chosen recipe

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.StepAdapterViewHolder>{


    public interface OnStepListItemClickListener {
        void onStepListItemClick(Step item);
    }

    public List<Step> stepList;
    private OnStepListItemClickListener listener;
    private  String recipeName;

    public StepListAdapter(List<Step> stepList, OnStepListItemClickListener listener) {
        this.stepList = stepList;
        this.listener = listener;
    }

    @Override
    public StepListAdapter.StepAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.cardview_step, viewGroup, false);

        return new StepAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StepAdapterViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Step step = stepList.get(position);
        int stepId = step.getId();
        String stepShortDescription = step.getShortDescription();
        if (stepId == 0) {
            holder.stepShortDescription.setText(stepShortDescription);
        } else {
            holder.stepShortDescription.setText(String.valueOf(stepId) + ".\u0020 " + stepShortDescription);
        }
        holder.bind(stepList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        if (null == stepList) return 0;
        return stepList.size();
    }

    // Define viewholder
    public class StepAdapterViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView stepShortDescription;

        public StepAdapterViewHolder(final View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardview_step);
            stepShortDescription = itemView.findViewById(R.id.tv_step_short_description);
        }

        public void bind(final Step item, final OnStepListItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onStepListItemClick(item);
                }
            });
        }
    }

    // Helper method to set the actual recipes list into the recyclerview on the activity
    public void setStepList(ArrayList<Step> steps) {
        stepList = steps;
        notifyDataSetChanged();
    }

}

