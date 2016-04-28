package org.eclipse.viatra.transformation.evm.api.adapter;

import java.util.List;

import com.google.common.collect.Lists;

public enum AdaptableEVMFactory {
    INSTANCE;
    private List<AdaptableEVM> adaptableEVMInstances = Lists.newArrayList();
    private List<IAdaptableEVMFactoryListener> listeners = Lists.newArrayList();
    
    public List<AdaptableEVM> getAdaptableEVMInstances() {
        return adaptableEVMInstances;
    }
    public AdaptableEVM getAdaptableEVMInstance(String id) {
        for (AdaptableEVM adaptableEVM : adaptableEVMInstances) {
            if(adaptableEVM.getIdentifier().equals(id)){
                return adaptableEVM;
            }
        }
        return null;
    }

    public AdaptableEVM createAdaptableEVM(){
        return createAdaptableEVM("AdaptableEVM_"+System.currentTimeMillis());
    }
    
    public void disposeAdaptableEVM(AdaptableEVM evm){
        adaptableEVMInstances.remove(evm);
        notifyListeners();
    }
    
    public AdaptableEVM createAdaptableEVM(String id){
        AdaptableEVM adaptableEVM = new AdaptableEVM(id);
        adaptableEVMInstances.add(adaptableEVM);
        notifyListeners();
        return adaptableEVM;
    }
    
    public void registerListener(IAdaptableEVMFactoryListener listener){
        if(!listeners.contains(listener)){
            listeners.add(listener);
            notifyListeners();
        }
    }
    protected void notifyListeners() {
        for (IAdaptableEVMFactoryListener adaptableEVMFactoryListener : listeners) {
            adaptableEVMFactoryListener.adaptableEVMPoolChanged(Lists.newArrayList(adaptableEVMInstances));
        }
    }
    
    public void unRegisterListener(IAdaptableEVMFactoryListener listener){
        if(listeners.contains(listener)){
            listeners.remove(listener);
        }
    }
    
}