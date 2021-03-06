package org.eclipse.viatra.addon.viewers.tooling.ui.zest;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


public class ViewersZestToolingPlugin extends AbstractUIPlugin {


    // The plug-in ID
    public static final String PLUGIN_ID = "org.eclipse.viatra.addon.viewers.tooling.ui.zest"; //$NON-NLS-1$

    // The shared instance
    private static ViewersZestToolingPlugin plugin;

    /**
     * The constructor
     */
    public ViewersZestToolingPlugin() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static ViewersZestToolingPlugin getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in relative path
     * 
     * @param path
     *            the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
}
