package com.example.businessapp.ui.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.businessapp.R
import com.example.businessapp.databinding.ItemOnboardingBinding
import com.google.android.gms.maps3d.model.ImageView

class OnboardingAdapter(
    private val images: List<Int>,
    private val titles: List<String>,
    private val descs: List<String>,
    private val listener: OnboardingClickListener
) : RecyclerView.Adapter<OnboardingAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemOnboardingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.imgOnboard.setImageResource(images[position])
        holder.binding.txtTitle.text = titles[position]
        holder.binding.txtDesc.text = descs[position]
        val color = when(position){
            0 -> R.color.green
            1 -> R.color.orange
            else -> R.color.yellow
        }
        when(position){
            0 -> {
               holder.binding.pageone.setBackgroundResource(R.drawable.dot_selected)
               holder.binding.pagetwo.setBackgroundResource(R.drawable.dot_unselected)
               holder.binding.pagethree.setBackgroundResource(R.drawable.dot_unselected)

            }
            1 -> {
                holder.binding.pageone.setBackgroundResource(R.drawable.dot_unselected)
                holder.binding.pagetwo.setBackgroundResource(R.drawable.dot_selected)
                holder.binding.pagethree.setBackgroundResource(R.drawable.dot_unselected)
            }
            else -> {
                holder.binding.pageone.setBackgroundResource(R.drawable.dot_unselected)
                holder.binding.pagetwo.setBackgroundResource(R.drawable.dot_unselected)
                holder.binding.pagethree.setBackgroundResource(R.drawable.dot_selected)
            }
        }

        holder.binding.btnJoin.setBackgroundColor(
            ContextCompat.getColor(holder.itemView.context, color)
        )
        holder.binding.btnLogin.setOnClickListener {
            listener.onLoginClick()
        }
    }

    override fun getItemCount(): Int = images.size

    interface OnboardingClickListener {
        fun onLoginClick()
    }
}