package core.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import core.config.ResourceConfig;
import core.config.ResourceFileConfig;
import core.security.AccessRight;
import core.security.ResourceViews;
import core.util.CollectionUtil;
import core.util.FileUtil;
import core.util.JsonUtil;
import core.util.Validator;
import core.web.util.DownloadBuilder;

/**
 * A representation of a series.
 * 
 * @author joro, petko, jani
 * 
 */
public class Series extends Resource {

	/**
	 * Default constructor.
	 */
	public Series() {
		setFormat(ResourceConfig.SERIES_FORMAT);
	}

	@Override
	public ResourceKind getKind() {
		return ResourceKind.SERIES;
	}

	@Override
	public Validator validate() {
		Validator res = super.validate();

		List<Contest> children = loadChildren();

		if (getDirectory() != null) {
			res.merge(validateFiles(children));
		}

		for (int i = 0; i < children.size(); i++) {
			Validator v = children.get(i).validate();
			res.merge(v, "child_" + i);
		}

		return res;
	}

	private Validator validateFiles(List<Contest> children) {
		Validator res = new Validator();

		assert getDirectory() != null;

		List<String> files = FileUtil.getDirectoryFileNames(getDirectory());

		String[] pattern = new String[] { ResourceFileConfig.ADDITIONAL_FILES,
				getFilename(), ResourceFileConfig.SECURITY_FILENAME_REGEX };

		List<String> allowed = CollectionUtil.filterRegex(files,
				CollectionUtil.join(pattern, "|"));
		
		if (allowed.size() + children.size() < files.size()) {
			res.addError("file_extra", "contains unknown files");
		}

		return res;
	}

	@Override
	public DownloadBuilder getDownloader(String login, AccessRight access) {
		String root = getDirectory().getName() + "/";

		String json = getJsonString(access);

		List<File> files = getFilesList(access);

		DownloadBuilder res = new DownloadBuilder();
		res.addInline(root + getFilename(), json.getBytes());
		res.addRefs(root, files);

		List<Resource> children = loadChildren();
		for (Resource child : children) {
			AccessRight childAccess = child.getAccessRight(login);
			DownloadBuilder d = child.getDownloader(login, childAccess);
			res.add(root, d);
		}

		return res;
	}

	@Override
	public String getJsonString(AccessRight access) {
		Class<?> view;

		if (access.includes(AccessRight.VIEW_FULL)) {
			view = ResourceViews.Private.class;
		} else {
			view = ResourceViews.Public.class;
		}

		return JsonUtil.objectToJsonString(this, view);
	}

	@Override
	public List<File> getFilesList(AccessRight access) {
		List<File> res = new ArrayList<File>();
		List<String> regex = new ArrayList<String>();
		if (access.includes(AccessRight.VIEW_FULL)) {
			regex.add(ResourceFileConfig.SECURITY_FILENAME_REGEX);
			regex.add(ResourceFileConfig.RESOURCE_PUBLIC_DIRECTORY_NAME);
			regex.add(ResourceFileConfig.RESOURCE_PRIVATE_DIRECTORY_NAME);
			regex.add(getFilename());
		} else if (access.includes(AccessRight.VIEW)) {
			regex.add(ResourceFileConfig.RESOURCE_PUBLIC_DIRECTORY_NAME);
			regex.add(getFilename());
		}
		String match = CollectionUtil.join(regex, "|");
		res = FileUtil.getDirectoryFiles(getDirectory(), match);
		return res;
	}
}
