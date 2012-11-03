/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataTransferObjects;

/**
 *
 * @author kenny
 */
public class SchedulePractionerDto extends AbstractDto {
    private PractionerDto practioner;
    private int startingTime;
    private int endingTime;
    private AppointmentDto[] appointments;
}
