/*
* generated by Xtext
*/
package org.palladiosimulator.commons.stoex.validation;

import org.eclipse.xtext.validation.Check;

import de.uka.ipd.sdq.probfunction.ProbabilityMassFunction;
import de.uka.ipd.sdq.probfunction.ProbfunctionPackage;
import de.uka.ipd.sdq.probfunction.Sample;
import de.uka.ipd.sdq.stoex.BoolLiteral;
import de.uka.ipd.sdq.stoex.BooleanOperations;
import de.uka.ipd.sdq.stoex.BooleanOperatorExpression;
import de.uka.ipd.sdq.stoex.StoexPackage;
import de.uka.ipd.sdq.units.UnitCarryingElement;
import de.uka.ipd.sdq.units.UnitsPackage;

/**
 * Custom validation rules. 
 *
 * see http://www.eclipse.org/Xtext/documentation.html#validation
 */
public class StoExJavaValidator extends org.palladiosimulator.commons.stoex.validation.AbstractStoExJavaValidator {
    private static final double delta = 0.00001;

    /**
     * This is used as an example what else could be validated:
     * Checks if a part of an AND-Operation is 'false' and so the expression can never be true.
     * @param expr
     */
    @Check
    public void checkBooleanUnerfuellbar(BooleanOperatorExpression expr){
        if(expr.getOperation()==BooleanOperations.AND){
            if(expr.getRight() instanceof BoolLiteral && !((BoolLiteral)expr.getRight()).isValue()){
                warning("AND-Expression can never be true.", StoexPackage.eINSTANCE.getBooleanOperatorExpression_Right());
            }
            if(expr.getLeft() instanceof BoolLiteral && !((BoolLiteral)expr.getLeft()).isValue()){
                warning("AND-Expression can never be true.", StoexPackage.eINSTANCE.getBooleanOperatorExpression_Left());
            }
        }
    }
    
   @Check
   public void addUnitWarnings(UnitCarryingElement elem){
       if(elem.getUnit()!=null){
           warning("Units are currently not solved!", UnitsPackage.eINSTANCE.getUnitCarryingElement_Unit());
       }
   }
   
   @Check
   public void checkSamplesSumUpToOne(ProbabilityMassFunction pmf){
       double sum = 0;
       for(Sample<?> s : pmf.getSamples()){
           sum += s.getProbability();
       }
       if(sum < 1.0-delta || sum > 1.0+delta){
           warning("Probabilities should sum up to 1.0!", ProbfunctionPackage.eINSTANCE.getProbabilityMassFunction_Samples());
       }
   }
}
