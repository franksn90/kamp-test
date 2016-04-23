/**
 */
package edu.kit.ipd.sdq.kamp.model.modificationmarks.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import de.uka.ipd.sdq.pcm.repository.RequiredRole;
import edu.kit.ipd.sdq.kamp.model.modificationmarks.ModifyRequiredRole;
import edu.kit.ipd.sdq.kamp.model.modificationmarks.modificationmarksPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Modify Required Role</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link edu.kit.ipd.sdq.kamp.model.modificationmarks.impl.ModifyRequiredRoleImpl#getRequiredrole <em>Requiredrole</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ModifyRequiredRoleImpl extends ModificationImpl implements ModifyRequiredRole {
	/**
	 * The cached value of the '{@link #getRequiredrole() <em>Requiredrole</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequiredrole()
	 * @generated
	 * @ordered
	 */
	protected RequiredRole requiredrole;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModifyRequiredRoleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return modificationmarksPackage.Literals.MODIFY_REQUIRED_ROLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RequiredRole getRequiredrole() {
		if (requiredrole != null && requiredrole.eIsProxy()) {
			InternalEObject oldRequiredrole = (InternalEObject)requiredrole;
			requiredrole = (RequiredRole)eResolveProxy(oldRequiredrole);
			if (requiredrole != oldRequiredrole) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, modificationmarksPackage.MODIFY_REQUIRED_ROLE__REQUIREDROLE, oldRequiredrole, requiredrole));
			}
		}
		return requiredrole;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RequiredRole basicGetRequiredrole() {
		return requiredrole;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRequiredrole(RequiredRole newRequiredrole) {
		RequiredRole oldRequiredrole = requiredrole;
		requiredrole = newRequiredrole;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, modificationmarksPackage.MODIFY_REQUIRED_ROLE__REQUIREDROLE, oldRequiredrole, requiredrole));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case modificationmarksPackage.MODIFY_REQUIRED_ROLE__REQUIREDROLE:
				if (resolve) return getRequiredrole();
				return basicGetRequiredrole();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case modificationmarksPackage.MODIFY_REQUIRED_ROLE__REQUIREDROLE:
				setRequiredrole((RequiredRole)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case modificationmarksPackage.MODIFY_REQUIRED_ROLE__REQUIREDROLE:
				setRequiredrole((RequiredRole)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case modificationmarksPackage.MODIFY_REQUIRED_ROLE__REQUIREDROLE:
				return requiredrole != null;
		}
		return super.eIsSet(featureID);
	}

} //ModifyRequiredRoleImpl