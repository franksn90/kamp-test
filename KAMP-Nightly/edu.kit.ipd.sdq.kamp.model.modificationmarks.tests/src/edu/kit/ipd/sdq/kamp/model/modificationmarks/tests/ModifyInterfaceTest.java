/**
 */
package edu.kit.ipd.sdq.kamp.model.modificationmarks.tests;

import edu.kit.ipd.sdq.kamp.model.modificationmarks.ModifyInterface;
import edu.kit.ipd.sdq.kamp.model.modificationmarks.modificationmarksFactory;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Modify Interface</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class ModifyInterfaceTest extends ModificationTest {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(ModifyInterfaceTest.class);
	}

	/**
	 * Constructs a new Modify Interface test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModifyInterfaceTest(String name) {
		super(name);
	}

	/**
	 * Returns the fixture for this Modify Interface test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected ModifyInterface getFixture() {
		return (ModifyInterface)fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(modificationmarksFactory.eINSTANCE.createModifyInterface());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
	@Override
	protected void tearDown() throws Exception {
		setFixture(null);
	}

} //ModifyInterfaceTest