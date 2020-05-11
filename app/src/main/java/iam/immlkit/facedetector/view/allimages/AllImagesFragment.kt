package iam.immlkit.facedetector.view.allimages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import iam.immlkit.facedetector.R
import iam.immlkit.facedetector.databinding.FragmentImageListBinding
import iam.immlkit.facedetector.model.rxbus.LiveDataEventBus
import iam.immlkit.facedetector.view.BaseFragment
import iam.immlkit.facedetector.view.imageadapter.ImagesAdapter
import iam.immlkit.facedetector.view.itemdecorations.GridItemDecoration
import iam.immlkit.facedetector.viewmodel.images.AllImagesViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class AllImagesFragment : BaseFragment() {

    companion object{

        private val SPANS_COUNT = 3

        fun newInstance():AllImagesFragment{
            val bundle = Bundle()
            val fragment = AllImagesFragment().apply {
                arguments = bundle
            }
            return fragment
        }
    }

    //lazy inity to view model so it will be immutable
    private val viewModel: AllImagesViewModel by viewModel()

    private lateinit var binding: FragmentImageListBinding
    private val adapter: ImagesAdapter by lazy { ImagesAdapter{Toast.makeText(activity, it.path, Toast.LENGTH_LONG).show()} }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_image_list,container,false)
        initRecycler()

        return binding.root
    }

    private fun initRecycler() {
        binding.rvImages.addItemDecoration(GridItemDecoration(resources.getDimension(R.dimen.grid_decorator_space).toInt(),SPANS_COUNT))
        binding.rvImages.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()
    }

    /**
     * Update UI according to data streams
     */
    private fun observeData() {
        //observe for updating images list
        viewModel.allImages.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        LiveDataEventBus.subscribe(viewLifecycleOwner,String::class.java) {
            Toast.makeText(activity,it,Toast.LENGTH_LONG).show()
        }
    }
}