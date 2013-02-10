package model.theory;

import java.util.HashSet;
import model.Column;
import java.util.Set;

// -------------------------------------------------------------------------
/**
 * This class represents attention levels of ideas within a Memory object due to
 * current input on a trained or untrained Region object.
 *
 * @author Huanqing
 * @version Jan 14, 2013
 */
public class MemoryClassifier
{
    private Set<Column> regionOutput;
    private Memory      memory;


    // ----------------------------------------------------------
    /**
     * Get private set of Column objects that become active after the spatial
     * pooler algorithm executed on a Region object.
     *
     * @return HashSet of Column objects.
     */
    public Set<Column> getRegionOutput()
    {
        return this.regionOutput;
    }


    // ----------------------------------------------------------
    /**
     * Create a new MemoryClassifier object.
     *
     * @param memory
     */
    public MemoryClassifier(Memory memory)
    {
        this.regionOutput = new HashSet<Column>();
        this.memory = memory;
    }


    // ----------------------------------------------------------
    /**
     * Get private Memory object representing all trained ideas.
     *
     * @return Memory object.
     */
    public Memory getMemory()
    {
        return this.memory;
    }


    // ----------------------------------------------------------
    /**
     * Update all Idea objects within Memory with any different Columns that are
     * becoming active. No Column objects are removed from any Idea objects.
     *
     * @param regionOutput
     *            Active Columns of the Region object after executing the
     *            spatial pooler algorithm once.
     */
    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     *
     * @param regionOutput1
     */
    public void updateIdeas(Set<Column> regionOutput1)
    {
        // update Region output with it's activeColumns
        this.regionOutput = regionOutput1;

        // compare which set of columns in memories has the greatest
        // intersection with regionOutput
        Set<Column> intersectionSet = new HashSet<Column>(this.regionOutput);

        // iterate through all ideas in memory
        for (Idea idea : this.memory.getIdeas())
        {
            // intersection between two sets of Column objects
            intersectionSet.retainAll(idea.getColumns());
            int intersectionSetSize = intersectionSet.size();

            // compute overlap of Region output with ideas in memory
            float attention =
                ((float)intersectionSetSize)
                    / ((float)idea.getColumns().size());
            float attentionPercentage = attention * 100;

            // Because a Idea object in the HashSet will be changed and thus
            // have it's hash code changed, the Idea object must be returned,
            // the original object removed from the HashSet, the object's
            // fields changed, and then add it back to the HashSet.
            this.memory.removeIdea(idea);
            idea.setAttentionPercentage(attentionPercentage);
            this.memory.addIdea(idea);
        }
    }


    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n=============================");
        stringBuilder.append("\n--------Brain Activity-------");
        for (Idea idea : this.memory.getIdeas())
        {
            stringBuilder.append("\n" + idea.getName() + ": "
                + idea.getAttentionPercentage() + "%");
        }
        stringBuilder.append("\nRegion ouput Column number: ");
        stringBuilder.append(this.regionOutput.size());
        stringBuilder.append("\n=============================");
        String brainActivityInformation = stringBuilder.toString();
        return brainActivityInformation;
    }
}