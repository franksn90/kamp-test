/**
 */
package de.uka.ipd.sdq.fieldOfActivityAnnotations.provider;


import de.uka.ipd.sdq.fieldOfActivityAnnotations.DesignPatternRole;
import de.uka.ipd.sdq.fieldOfActivityAnnotations.FieldOfActivityAnnotationsPackage;

import de.uka.ipd.sdq.identifier.provider.IdentifierItemProvider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * This is the item provider adapter for a {@link de.uka.ipd.sdq.fieldOfActivityAnnotations.DesignPatternRole} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class DesignPatternRoleItemProvider
extends IdentifierItemProvider
implements
	IEditingDomainItemProvider,
	IStructuredItemContentProvider,
	ITreeItemContentProvider,
	IItemLabelProvider,
	IItemPropertySource {
/**
 * This constructs an instance from a factory and a notifier.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public DesignPatternRoleItemProvider(AdapterFactory adapterFactory) {
	super(adapterFactory);
}

/**
 * This returns the property descriptors for the adapted class.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
@Override
public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
	if (itemPropertyDescriptors == null) {
		super.getPropertyDescriptors(object);

		addProvidedRolePropertyDescriptor(object);
		addComponentPropertyDescriptor(object);
	}
	return itemPropertyDescriptors;
}

/**
 * This adds a property descriptor for the Provided Role feature.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
protected void addProvidedRolePropertyDescriptor(Object object) {
	itemPropertyDescriptors.add
		(createItemPropertyDescriptor
			(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
			 getResourceLocator(),
			 getString("_UI_DesignPatternRole_providedRole_feature"),
			 getString("_UI_PropertyDescriptor_description", "_UI_DesignPatternRole_providedRole_feature", "_UI_DesignPatternRole_type"),
			 FieldOfActivityAnnotationsPackage.Literals.DESIGN_PATTERN_ROLE__PROVIDED_ROLE,
			 true,
			 false,
			 true,
			 null,
			 null,
			 null));
}

/**
 * This adds a property descriptor for the Component feature.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
protected void addComponentPropertyDescriptor(Object object) {
	itemPropertyDescriptors.add
		(createItemPropertyDescriptor
			(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
			 getResourceLocator(),
			 getString("_UI_DesignPatternRole_component_feature"),
			 getString("_UI_PropertyDescriptor_description", "_UI_DesignPatternRole_component_feature", "_UI_DesignPatternRole_type"),
			 FieldOfActivityAnnotationsPackage.Literals.DESIGN_PATTERN_ROLE__COMPONENT,
			 true,
			 false,
			 true,
			 null,
			 null,
			 null));
}

/**
 * This returns DesignPatternRole.gif.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
@Override
public Object getImage(Object object) {
	return overlayImage(object, getResourceLocator().getImage("full/obj16/DesignPatternRole"));
}

/**
 * This returns the label text for the adapted class.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
@Override
public String getText(Object object) {
	String label = ((DesignPatternRole)object).getId();
	return label == null || label.length() == 0 ?
		getString("_UI_DesignPatternRole_type") :
		getString("_UI_DesignPatternRole_type") + " " + label;
}

/**
 * This handles model notifications by calling {@link #updateChildren} to update any cached
 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
@Override
public void notifyChanged(Notification notification) {
	updateChildren(notification);
	super.notifyChanged(notification);
}

/**
 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
 * that can be created under this object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
@Override
protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
	super.collectNewChildDescriptors(newChildDescriptors, object);
}

/**
 * Return the resource locator for this item provider's resources.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
@Override
public ResourceLocator getResourceLocator() {
	return FieldOfActivityAnnotationsEditPlugin.INSTANCE;
}

}
