/**
 */
package edu.kit.ipd.sdq.kamp.model.fieldofactivityannotations;

import de.uka.ipd.sdq.pcm.core.composition.AssemblyConnector;

import de.uka.ipd.sdq.pcm.repository.OperationProvidedRole;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Integration Test Case</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link edu.kit.ipd.sdq.kamp.model.fieldofactivityannotations.IntegrationTestCase#getParent <em>Parent</em>}</li>
 *   <li>{@link edu.kit.ipd.sdq.kamp.model.fieldofactivityannotations.IntegrationTestCase#getProvidedrole <em>Providedrole</em>}</li>
 *   <li>{@link edu.kit.ipd.sdq.kamp.model.fieldofactivityannotations.IntegrationTestCase#getAssemblyConnector <em>Assembly Connector</em>}</li>
 * </ul>
 * </p>
 *
 * @see edu.kit.ipd.sdq.kamp.model.fieldofactivityannotations.FieldofactivityannotationsPackage#getIntegrationTestCase()
 * @model
 * @generated
 */
public interface IntegrationTestCase extends TestCase {
	/**
	 * Returns the value of the '<em><b>Parent</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link edu.kit.ipd.sdq.kamp.model.fieldofactivityannotations.TestSpecification#getIntegrationTestCases <em>Integration Test Cases</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parent</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent</em>' container reference.
	 * @see #setParent(TestSpecification)
	 * @see edu.kit.ipd.sdq.kamp.model.fieldofactivityannotations.FieldofactivityannotationsPackage#getIntegrationTestCase_Parent()
	 * @see edu.kit.ipd.sdq.kamp.model.fieldofactivityannotations.TestSpecification#getIntegrationTestCases
	 * @model opposite="integrationTestCases" required="true" transient="false"
	 * @generated
	 */
	TestSpecification getParent();

	/**
	 * Sets the value of the '{@link edu.kit.ipd.sdq.kamp.model.fieldofactivityannotations.IntegrationTestCase#getParent <em>Parent</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent</em>' container reference.
	 * @see #getParent()
	 * @generated
	 */
	void setParent(TestSpecification value);

	/**
	 * Returns the value of the '<em><b>Providedrole</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Providedrole</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Providedrole</em>' reference.
	 * @see #setProvidedrole(OperationProvidedRole)
	 * @see edu.kit.ipd.sdq.kamp.model.fieldofactivityannotations.FieldofactivityannotationsPackage#getIntegrationTestCase_Providedrole()
	 * @model
	 * @generated
	 */
	OperationProvidedRole getProvidedrole();

	/**
	 * Sets the value of the '{@link edu.kit.ipd.sdq.kamp.model.fieldofactivityannotations.IntegrationTestCase#getProvidedrole <em>Providedrole</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Providedrole</em>' reference.
	 * @see #getProvidedrole()
	 * @generated
	 */
	void setProvidedrole(OperationProvidedRole value);

	/**
	 * Returns the value of the '<em><b>Assembly Connector</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Assembly Connector</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Assembly Connector</em>' reference.
	 * @see #setAssemblyConnector(AssemblyConnector)
	 * @see edu.kit.ipd.sdq.kamp.model.fieldofactivityannotations.FieldofactivityannotationsPackage#getIntegrationTestCase_AssemblyConnector()
	 * @model
	 * @generated
	 */
	AssemblyConnector getAssemblyConnector();

	/**
	 * Sets the value of the '{@link edu.kit.ipd.sdq.kamp.model.fieldofactivityannotations.IntegrationTestCase#getAssemblyConnector <em>Assembly Connector</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Assembly Connector</em>' reference.
	 * @see #getAssemblyConnector()
	 * @generated
	 */
	void setAssemblyConnector(AssemblyConnector value);

} // IntegrationTestCase