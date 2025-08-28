import SwiftUI
import Shared

struct ContentView: View {
    @StateObject private var llmViewModel = LlmViewModel()
    @State private var inputText = ""
    
    var body: some View {
        VStack(spacing: 20) {
            Text("LLM Chat")
                .font(.largeTitle)
                .padding()
            
            // State-based content
            switch llmViewModel.state {
            case is LlmState.Initial:
                VStack {
                    Text("Ready to initialize the model")
                    Button("Initialize Model") {
                        llmViewModel.initialize()
                    }
                    .buttonStyle(.borderedProminent)
                }
                
            case is LlmState.Loading:
                VStack {
                    ProgressView()
                    Text("Loading...")
                }
                
            case is LlmState.Ready:
                VStack(spacing: 16) {
                    TextField("Enter your question...", text: $inputText)
                        .textFieldStyle(.roundedBorder)
                    
                    Button("Ask") {
                        llmViewModel.infer(input: inputText)
                        inputText = ""
                    }
                    .buttonStyle(.borderedProminent)
                    .disabled(inputText.isEmpty)
                }
                
            case is LlmState.Error:
                VStack {
                    Text("Error occurred")
                        .foregroundColor(.red)
                    Button("Retry") {
                        llmViewModel.initialize()
                    }
                    .buttonStyle(.bordered)
                }
                
            case let answerState as LlmState.Answer:
                VStack(alignment: .leading, spacing: 16) {
                    Text("Answer:")
                        .font(.headline)
                    Text(answerState.answer)
                        .padding()
                        .background(Color.gray.opacity(0.1))
                        .cornerRadius(8)
                    
                    TextField("Ask another question...", text: $inputText)
                        .textFieldStyle(.roundedBorder)
                    
                    Button("Ask Again") {
                        llmViewModel.infer(input: inputText)
                        inputText = ""
                    }
                    .buttonStyle(.borderedProminent)
                    .disabled(inputText.isEmpty)
                }
                
            default:
                Text("Unknown state")
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
