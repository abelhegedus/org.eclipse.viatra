package system.queries.util;

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.eclipse.incquery.runtime.api.IncQueryEngine;
import org.eclipse.incquery.runtime.api.impl.BaseGeneratedEMFPQuery;
import org.eclipse.incquery.runtime.api.impl.BaseGeneratedEMFQuerySpecification;
import org.eclipse.incquery.runtime.exception.IncQueryException;
import org.eclipse.incquery.runtime.matchers.psystem.PBody;
import org.eclipse.incquery.runtime.matchers.psystem.PVariable;
import org.eclipse.incquery.runtime.matchers.psystem.annotations.PAnnotation;
import org.eclipse.incquery.runtime.matchers.psystem.basicdeferred.ExportedParameter;
import org.eclipse.incquery.runtime.matchers.psystem.basicenumerables.TypeBinary;
import org.eclipse.incquery.runtime.matchers.psystem.basicenumerables.TypeUnary;
import org.eclipse.incquery.runtime.matchers.psystem.queries.PParameter;
import org.eclipse.incquery.runtime.matchers.psystem.queries.QueryInitializationException;
import system.queries.JobTaskCorrespondenceMatch;
import system.queries.JobTaskCorrespondenceMatcher;

/**
 * A pattern-specific query specification that can instantiate JobTaskCorrespondenceMatcher in a type-safe way.
 * 
 * @see JobTaskCorrespondenceMatcher
 * @see JobTaskCorrespondenceMatch
 * 
 */
@SuppressWarnings("all")
public final class JobTaskCorrespondenceQuerySpecification extends BaseGeneratedEMFQuerySpecification<JobTaskCorrespondenceMatcher> {
  private JobTaskCorrespondenceQuerySpecification() {
    super(GeneratedPQuery.INSTANCE);
  }
  
  /**
   * @return the singleton instance of the query specification
   * @throws IncQueryException if the pattern definition could not be loaded
   * 
   */
  public static JobTaskCorrespondenceQuerySpecification instance() throws IncQueryException {
    try{
    	return LazyHolder.INSTANCE;
    } catch (ExceptionInInitializerError err) {
    	throw processInitializerError(err);
    }
  }
  
  @Override
  protected JobTaskCorrespondenceMatcher instantiate(final IncQueryEngine engine) throws IncQueryException {
    return JobTaskCorrespondenceMatcher.on(engine);
  }
  
  @Override
  public JobTaskCorrespondenceMatch newEmptyMatch() {
    return JobTaskCorrespondenceMatch.newEmptyMatch();
  }
  
  @Override
  public JobTaskCorrespondenceMatch newMatch(final Object... parameters) {
    return JobTaskCorrespondenceMatch.newMatch((system.Job) parameters[0], (process.Task) parameters[1]);
  }
  
  private static class LazyHolder {
    private final static JobTaskCorrespondenceQuerySpecification INSTANCE = make();
    
    public static JobTaskCorrespondenceQuerySpecification make() {
      return new JobTaskCorrespondenceQuerySpecification();					
    }
  }
  
  private static class GeneratedPQuery extends BaseGeneratedEMFPQuery {
    private final static JobTaskCorrespondenceQuerySpecification.GeneratedPQuery INSTANCE = new GeneratedPQuery();
    
    @Override
    public String getFullyQualifiedName() {
      return "system.queries.JobTaskCorrespondence";
    }
    
    @Override
    public List<String> getParameterNames() {
      return Arrays.asList("Job","Task");
    }
    
    @Override
    public List<PParameter> getParameters() {
      return Arrays.asList(new PParameter("Job", "system.Job"),new PParameter("Task", "process.Task"));
    }
    
    @Override
    public Set<PBody> doGetContainedBodies() throws QueryInitializationException {
      Set<PBody> bodies = Sets.newLinkedHashSet();
      try {
      {
      	PBody body = new PBody(this);
      	PVariable var_Job = body.getOrCreateVariableByName("Job");
      	PVariable var_Task = body.getOrCreateVariableByName("Task");
      	PVariable var_TaskId = body.getOrCreateVariableByName("TaskId");
      	body.setExportedParameters(Arrays.<ExportedParameter>asList(
      		new ExportedParameter(body, var_Job, "Job"),
      				
      		new ExportedParameter(body, var_Task, "Task")
      	));
      	new TypeUnary(body, var_Task, getClassifierLiteral("http://process/1.0", "Task"), "http://process/1.0/Task");
      	new TypeBinary(body, CONTEXT, var_Job, var_TaskId, getFeatureLiteral("http://system/1.0", "Job", "taskIds"), "http://system/1.0/Job.taskIds");
      	new TypeBinary(body, CONTEXT, var_Task, var_TaskId, getFeatureLiteral("http://process/1.0", "ProcessElement", "id"), "http://process/1.0/ProcessElement.id");
      	bodies.add(body);
      }
      	{
      	PAnnotation annotation = new PAnnotation("QueryBasedFeature");
      	annotation.addAttribute("feature", "tasks");
      	addAnnotation(annotation);
      }
      	// to silence compiler error
      	if (false) throw new IncQueryException("Never", "happens");
      } catch (IncQueryException ex) {
      	throw processDependencyException(ex);
      }
      return bodies;
    }
  }
}
