package core.security;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.model.Resource;
import core.model.ResourceUtil;

/**
 * Represents a simple wrapper, for specific rights upon a resource.
 * 
 * @author jani
 * 
 */
public class ResourceGrants {
	private String resourceId;
	private boolean canViewBasic;
	private boolean canViewDetails;
	private boolean canEdit;
	private boolean canDelete;

	/**
	 * Created a wrapper with the specified arguments.
	 * 
	 * @param resourceId
	 *            ID of resource.
	 * @param canViewBasic
	 *            If resource can be viewed.
	 * @param canViewDetails
	 *            If resource can be viewed with details
	 * @param canEdit
	 *            If resource can be edited.
	 * @param canDelete
	 *            If resource can be deleted.
	 */
	public ResourceGrants(String resourceId, boolean canViewBasic,
			boolean canViewDetails, boolean canEdit, boolean canDelete) {
		super();
		this.resourceId = resourceId;
		this.canViewBasic = canViewBasic;
		this.canViewDetails = canViewDetails;
		this.canEdit = canEdit;
		this.canDelete = canDelete;
	}

	/**
	 * Gets the resource ID.
	 * 
	 * @return Resource ID.
	 */
	public String getResourceId() {
		return resourceId;
	}

	/**
	 * Gets if the resource can be viewed.
	 * 
	 * @return Whether the resource can be viewed.
	 */
	public boolean isCanViewBasic() {
		return canViewBasic;
	}

	/**
	 * Gets if the resource can be viewed with details.
	 * 
	 * @return Whether the resource can be viewed with details.
	 */
	public boolean isCanViewDetails() {
		return canViewDetails;
	}

	/**
	 * Gets if the resource can be edited.
	 * 
	 * @return Whether the resource can be edited.
	 */
	public boolean isCanEdit() {
		return canEdit;
	}

	/**
	 * Gets if the resource can be deleted.
	 * 
	 * @return Whether the resource can be deleted.
	 */
	public boolean isCanDelete() {
		return canDelete;
	}

	/**
	 * Builds a map of {@link ResourceGrants}, upon a list of resources.
	 * 
	 * @param seriesRoot
	 *            Root directory for series.
	 * 
	 * @param parentDir
	 *            Common parent directory of resources in list.
	 * @param list
	 *            List of resources on which to build a map.
	 * @param login
	 *            Login to build a map for.
	 * @return A map of {@link ResourceGrants}.
	 */
	public static Map<String, ResourceGrants> createMap(File seriesRoot,
			File parentDir, List<Resource> list, String login) {
		Map<String, ResourceGrants> res = new HashMap<String, ResourceGrants>();
		AccessRight parentAccess = ResourceUtil.getAccessRight(seriesRoot,
				parentDir, login);

		for (Resource resource : list) {
			File directory = new File(parentDir, resource.getId());
			AccessRight right = ResourceUtil.getAccessRight(seriesRoot,
					directory, login);
			boolean canViewBasic = right.includes(AccessRight.LIST);
			boolean canViewDetails = right.includes(AccessRight.VIEW);
			boolean canEdit = right.includes(AccessRight.VIEW_FULL);
			boolean canDelete = parentAccess.includes(AccessRight.VIEW_FULL);
			ResourceGrants grant = new ResourceGrants(resource.getId(),
					canViewBasic, canViewDetails, canEdit, canDelete);
			res.put(resource.getId(), grant);
		}
		return res;
	}
}
