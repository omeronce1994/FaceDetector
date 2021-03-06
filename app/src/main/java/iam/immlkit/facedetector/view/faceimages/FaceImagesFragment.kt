package iam.immlkit.facedetector.view.faceimages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import iam.immlkit.facedetector.R
import iam.immlkit.facedetector.databinding.FragmentImageListBinding
import iam.immlkit.facedetector.model.ServiceLocator
import iam.immlkit.facedetector.view.BaseFragment
import iam.immlkit.facedetector.view.allimages.AllImagesFragment
import iam.immlkit.facedetector.view.imageadapter.ImagesAdapter
import iam.immlkit.facedetector.view.itemdecorations.GridItemDecoration
import iam.immlkit.facedetector.viewmodel.images.AllImagesViewModel
import iam.immlkit.facedetector.viewmodel.images.FaceImagesViewModel
import iam.immlkit.facedetector.viewmodel.images.ImagesViewModelFactory
import java.lang.IllegalStateException

class FaceImagesFragment:BaseFragment() {

    companion object{

        private val SPANS_COUNT = 3

        fun newInstance(): FaceImagesFragment {
            val bundle = Bundle()
            val fragment = FaceImagesFragment().apply {
                arguments = bundle
            }
            return fragment
        }
    }

    //lazy inity to view model so it will be immutable
    private val viewModel: FaceImagesViewModel by lazy {
        if(activity==null)
            throw IllegalStateException("Must access view model only after onAttached so that activity is not null")
        val act: FragmentActivity = activity!!
        val application = act.application
        val db = ServiceLocator.getAppDB(act)
        val repo = ServiceLocator.getFaceImagesRepository(db)
        val factory = ServiceLocator.getImagesFactory(application,repo)
        ViewModelProviders.of(this,factory).get(FaceImagesViewModel::class.java)
    }

    private lateinit var binding: FragmentImageListBinding
    private val adapter: ImagesAdapter by lazy { ImagesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image_list,container,false)
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

    private fun observeData() {
        viewModel.allFacesImages.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
    }
}